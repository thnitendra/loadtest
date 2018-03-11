package simulations

import java.io.{FileNotFoundException, PrintWriter, StringWriter}

import io.gatling.core.Predef._
import org.json.JSONException

import scala.io.Source
//import io.gatling.core.structure.PopulatedScenarioBuilder
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.controller.throttle.ThrottleStep
import io.gatling.core.structure.PopulationBuilder
import org.json.{JSONArray, JSONObject}
import scenarios._
import settings._

import scala.collection.mutable.ArraySeq
import scala.concurrent.duration._

/**
  * https://gatling.io/docs/2.3/general/simulation_setup/
  */
class Main extends Simulation {
    println("Duration: " + Props.duration)
    def byNode(args :JSONArray, index: Int): Int  = {
        Props.byNode(args.getInt(0))
    }

    def resolveInjection(step: JSONObject): InjectionStep  = {
        var stepType = step.getString("type")
        var stepArgs = step.getJSONArray("args")
        stepType match {
            case "rampUsers" => rampUsers(Props.byNode(stepArgs.getInt(0))) over(stepArgs.getInt(1) seconds)
            case "heavisideUsers" => heavisideUsers(Props.byNode(stepArgs.getInt(0))) over(stepArgs.getInt(1) seconds)
            case "atOnceUsers" => atOnceUsers(Props.byNode(stepArgs.getInt(0)))
            case "constantUsersPerSec" => constantUsersPerSec(Props.byNode(stepArgs.getInt(0))) during(stepArgs.getInt(1) seconds)
            case "rampUsersPerSec" => rampUsersPerSec(Props.byNode(stepArgs.getInt(0))) to(Props.byNode(stepArgs.getInt(1))) during(stepArgs.getInt(2) seconds)
            case "nothingFor" => nothingFor(stepArgs.getInt(0) seconds)
            case "splitUsersByTime" => splitUsers(Props.byNode(stepArgs.getInt(0))) into (resolveInjection(stepArgs.getJSONObject(1))) separatedBy (stepArgs.getInt(2) seconds)
            case "splitUsersByTask" => splitUsers(Props.byNode(stepArgs.getInt(0))) into (resolveInjection(stepArgs.getJSONObject(1))) separatedBy (resolveInjection(stepArgs.getJSONObject(2)))
        }
    }

    def resolveThrottle(step: JSONObject): ThrottleStep  = {
        var stepType = step.getString("type")
        var stepArgs = step.getJSONArray("args")
        stepType match {
            case "reachRps" => reachRps(Props.byNode(stepArgs.getInt(0))) in (stepArgs.getInt(1) seconds)
            case "jumpToRps" => jumpToRps(Props.byNode(stepArgs.getInt(0)))
            case "holdFor" => holdFor(stepArgs.getInt(0))
        }
    }

    def printException(e: Exception) = {
        val sw = new StringWriter()
        e.printStackTrace(new PrintWriter(sw))
        println(sw.toString)
    }

    def scnList() : Seq[PopulationBuilder] = {
        var testList: JSONArray = null
        try {
            val rawTestList: String = Source.fromFile(Props.testcase).getLines.mkString
            testList = new JSONArray(rawTestList)
        } catch {
            case fe: FileNotFoundException =>
                println("Could not find a valid Test Case to execute.")
                printException(fe)
                System.exit(1)
            case e: Exception =>
                printException(e)
        }

        var scnCount :Int = testList.length()
        var scnList = new ArraySeq[PopulationBuilder](scnCount)
        for (i <- 0 until scnCount) {
            var testCase = testList.getJSONObject(i)
            println("Scenario: " + testCase.getString("api"))

            var injectStepList: ArraySeq[InjectionStep] = null
            try {
                var rampU :Int  = testCase.getInt("rampUsers")
                var rampT :Int  = testCase.getInt("rampTime")
                injectStepList = new ArraySeq[InjectionStep](1)
                injectStepList(0) = rampUsers(Props.byNode(rampU)) over (rampT seconds)
            } catch {
                case e: JSONException =>
                    val injectionSteps = testCase.getJSONArray("inject")
                    injectStepList = new ArraySeq[InjectionStep](injectionSteps.length())
                    for (j <- 0 until injectionSteps.length()) {
                        var step = injectionSteps.getJSONObject(j)
                        injectStepList(j) = resolveInjection(step)
                    }
            }

            var throttlingStepList: ArraySeq[ThrottleStep] = null
            try {
                var qps :Int = testCase.getInt("qps")
                var rampT :Int = testCase.getInt("rampTime")
                throttlingStepList = new ArraySeq[ThrottleStep](2)
                throttlingStepList(0) = reachRps(Props.byNode(qps)) in (rampT seconds)
                throttlingStepList(1) = holdFor(Props.duration seconds)
            } catch {
                case e: JSONException =>
                    val throttlingSteps = testCase.getJSONArray("throttle")
                    throttlingStepList = new ArraySeq[ThrottleStep](throttlingSteps.length())
                    for (k <- 0 until throttlingSteps.length()) {
                        var step = throttlingSteps.getJSONObject(k)
                        throttlingStepList(k) = resolveThrottle(step)
                    }
            }

            //scnList(i) = Scenarios.scenarioNames(testCase.getString("api")).inject(injectStepList: _*)
            scnList(i) = Scenarios.scenarioNames(testCase.getString("api")).inject(injectStepList: _*).throttle(throttlingStepList: _*)
        }

        scnList
    }

    setUp(scnList:_*).protocols(Props.httpConf)
}
