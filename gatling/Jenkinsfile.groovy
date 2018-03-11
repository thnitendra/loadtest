@NonCPS
List getNodes() {
    return Jenkins.instance.slaves.findAll { it.getLabelString() == "GATLING-SLAVE" && it.getComputer().isOnline() }
}

nodeIndex = 0
@NonCPS
int nextNodeIndex() {
    return nodeIndex++
}

node("GATLING-MASTER") {
    def gitBranch = "gatling"
    def duration = env["Duration"] // seconds
    def testcase = env["Test Case"]
    def proxy = env["Proxy"]
    def endpoints = env["Endpoints"]
    def authToken = env["OAuth Token"]

    def nodesCount = getNodes().size()
    if(env["Nodes Count"]) {
        nodesCount = env["Nodes Count"].toInteger()
    }

    def lstEndpoints = endpoints.split(",").toList()
    int bucketSize = lstEndpoints.size() / nodesCount
    def lstEndpointsByNode
    if(bucketSize) {
        lstEndpointsByNode = lstEndpoints.collate(bucketSize).take(nodesCount)
        int j = lstEndpointsByNode.size() * bucketSize
        for (int i = 0; i < (lstEndpoints.size() % nodesCount); ++i) {
            lstEndpointsByNode[i] << lstEndpoints[j + i]
        }
    } else {
        lstEndpointsByNode = new ArrayList(nodesCount)
        (1..nodesCount).each {
            lstEndpointsByNode << lstEndpoints
        }
    }

    def jvmArgs = "-Dtestcase=" + testcase
    jvmArgs += " -Dduration=" + duration
    jvmArgs += " -DauthToken=" + authToken
    jvmArgs += " -DnodesCount=" + nodesCount

    if(proxy) {
        prx = proxy.split(":")
        jvmArgs += " -Dproxyhost=" + prx[0] + " -Dproxyport=" + prx[1]
    }


    if(env["Results Archive"] == "NFS") {
        env.resultsDir = "/nfs/path/for/report/archive/"
    } else if(env["Results Archive"] == "Stash") {
        env.resultsDir = "${workspace}"
    }

    def clients = [:]
    for (int i = 0; i < nodesCount; i++) {
        def index = i
        clients["client${index}"] = {
            node("GATLING-SLAVE") {
                stage("Executing Test") {
                    git changelog: false, credentialsId: 'jenkins_credentials_id', poll: false, url: 'git@github.com:thnitendra/loadtest.git', branch: "${gitBranch}"
                    dir("gatling"){
                        sh "if [ -d build/reports ]; then rm -rf build/reports/*; fi"
                        int nodeIndex = nextNodeIndex()
                        env.jvmArgs = jvmArgs + " -DnodeIndex=${nodeIndex} -Dendpoints=" + lstEndpointsByNode[nodeIndex].join(",")
                        sh "./gradlew clean gatling -Dgatling-noReports=true -Psim=simulations.Main -PjvmArgs='" + env.jvmArgs + "'"
                    }
                }
                stage('Stash Simulation log') {
                    sh "if [ -d ${resultsDir}/gatling/results* ]; then rm -rf ${resultsDir}/gatling/results; fi"
                    sh "mkdir ${resultsDir}/gatling/results"
                    sh "mv gatling/build/reports/**/*.log ${resultsDir}/gatling/results/simulation${index}.log"
                    if(env["Results Archive"] == "Stash") {
                        stash includes: "gatling/results/simulation${index}.log", name: "log${index}"
                    }
                }
            }
        }
    }

    timestamps {
        parallel clients
    }
    stage('Generating Report') {
        timestamps {
            step([$class: 'WsCleanup'])

            //def gatlingHome = tool 'gatling'
            env.gatlingHome = "~/gatling"
            if (env["Results Archive"] == "Stash") {
                for (int i = 0; i < nodesCount; i++) {
                    unstash "log${i}"
                }
            }
            env.ts = new Date().getTime()
            sh "mv ${resultsDir}/gatling/results ${resultsDir}/gatling/results-${ts}"
            sh "touch ${resultsDir}/gatling/results-${ts}"
            sh "${gatlingHome}/bin/gatling.sh --reports-only ${resultsDir}/gatling/results-${ts}"
            if (env["Results Archive"] == "NFS") {
                sh "ln -s ${resultsDir}/gatling gatling"
            }
            gatlingArchive()
            //publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'results', reportFiles: 'index.html', reportName: 'Stress Test Report'])
        }
    }
}

