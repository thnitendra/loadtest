#!/bin/bash

DIR=$(pwd)
TOTAL_QPS=$1
JMETER_ZIP_FILE=$2
JMETER_REMOTE_FOLDER_NAME="jmeter.loadtest"
JMETER_REMOTE_CAPACITY=$8 #Review
TARGET_HOST_START_SID=$3
TARGET_HOST_END_SID=$4
SCRIPT=$5
RUNNING_TIME_SEC=600	#Review
JMETER_HOST_START_SID=$6
JMETER_HOST_END_SID=$7
RAMPUP=$9
QPS_PER_TARGET_HOST=$((TOTAL_QPS / (TARGET_HOST_END_SID - TARGET_HOST_START_SID + 1)))

if [ "$QPS_PER_TARGET_HOST" -eq "0" ]; then
  QPS_PER_TARGET_HOST=1
  echo "Set QPS_PER_TARGET_HOST=1"
fi

if [[ -z $JMETER_REMOTE_FOLDER_NAME ]]; then
  echo "JMETER_REMOTE_FOLDER_NAME is empty"
  exit 1;
fi

if [[ -z $JMETER_HOST_START_SID ]]; then
  echo "JMETER_HOST_START_SID is empty"
  exit 1;
fi

if [[ -z $JMETER_HOST_END_SID ]]; then
  echo "JMETER_HOST_END_SID is empty"
  exit 1;
fi

if [ $JMETER_REMOTE_CAPACITY -lt $QPS_PER_TARGET_HOST ]; then
  echo "JMETER_REMOTE_CAPACITY=$JMETER_REMOTE_CAPACITY is less than QPS_PER_TARGET_HOST=$QPS_PER_TARGET_HOST"
  exit 1;
fi

echo "TOTAL_QPS=${TOTAL_QPS}, JMETER_ZIP_FILE=${JMETER_ZIP_FILE}, TARGET_HOST_START_SID=${TARGET_HOST_START_SID}, TARGET_HOST_END_SID=${TARGET_HOST_END_SID}, RUNNING_TIME_SEC=${RUNNING_TIME_SEC}, SCRIPT=${SCRIPT}"

#Cleanup
echo "Cleaning"
for host in $(seq ${JMETER_HOST_START_SID} ${JMETER_HOST_END_SID});do
  ssh jmeter_host_${host} "pkill -f java"
  ssh jmeter_host_${host} "rm -f ${JMETER_REMOTE_FOLDER_NAME}/result/*"
done

#echo "Deploying"
##Deploy script and jmx files
#for host in $(seq ${JMETER_HOST_START_SID} ${JMETER_HOST_END_SID});do
#    echo "deloying ${host}"
#    ssh jmeter_host_${host} "mkdir -p ~/${JMETER_REMOTE_FOLDER_NAME}"
#    scp ${JMETER_ZIP_FILE} jmeter_host_${host}:~/${JMETER_REMOTE_FOLDER_NAME}
#    ssh jmeter_host_${host} "cd ~/${JMETER_REMOTE_FOLDER_NAME};unzip -oq $(basename ${JMETER_ZIP_FILE});rm -f $(basename ${JMETER_ZIP_FILE})"
#done

echo "Running"
#Run
TARGET_SID=$TARGET_HOST_START_SID
JMETER_SID=$JMETER_HOST_START_SID
REAL_TOTAL_QPS=0
WORKERS=()
CURRENT_SLEEP_TIME_SEC=0
SLEEP_TIME_SEC=2
while [ $TARGET_SID -le $TARGET_HOST_END_SID ] && [ $JMETER_SID -le $JMETER_HOST_END_SID ]; do

  JMETER_REMOTE_CURRENT_CAPACITY=0
  while [ $JMETER_REMOTE_CURRENT_CAPACITY -lt $JMETER_REMOTE_CAPACITY ] && [ $TARGET_SID -le $TARGET_HOST_END_SID ]; do
    echo "Run on jmeter jmeter_host_${JMETER_SID} against target_host_${TARGET_SID}  for QPS ${QPS_PER_TARGET_HOST} Sleep time is ${9}"

    sleep $9;
    ssh jmeter_host_${JMETER_SID} "sleep 2;cd ${JMETER_REMOTE_FOLDER_NAME}; ./${SCRIPT} ${QPS_PER_TARGET_HOST} ${TARGET_SID}" &

    TARGET_SID=$((TARGET_SID + 1))
    CURRENT_SLEEP_TIME_SEC=$((CURRENT_SLEEP_TIME_SEC + SLEEP_TIME_SEC))
    JMETER_REMOTE_CURRENT_CAPACITY=$((JMETER_REMOTE_CURRENT_CAPACITY + QPS_PER_TARGET_HOST))
    REAL_TOTAL_QPS=$((REAL_TOTAL_QPS + QPS_PER_TARGET_HOST))
  done

  WORKERS+=(${JMETER_SID})
  JMETER_SID=$((JMETER_SID + 1))

done

RUNNING_TIME_SEC=$((RUNNING_TIME_SEC + CURRENT_SLEEP_TIME_SEC))

echo "REAL_TOTAL_QPS=${REAL_TOTAL_QPS}, WORKERS=${WORKERS[@]}"
echo "Sleeping...${RUNNING_TIME_SEC}secs"
sleep ${RUNNING_TIME_SEC}

for worker in "${WORKERS[@]}"; do
  ssh jmeter_host_${worker} "pkill -f java"
done

#Collecting Results
DATETIME=`date "+%Y%m%d%H%M%S"`
RESULT_FOLDER=results_qps${TOTAL_QPS}_${DATETIME}
mkdir -p ${RESULT_FOLDER}
for worker in "${WORKERS[@]}"; do
  RESULT_FILE=${RESULT_FOLDER}_${worker}.tgz
  ssh jmeter_host_${worker} "cd ${JMETER_REMOTE_FOLDER_NAME}/result;tar -zcpvf ${RESULT_FILE} *jtl"
  scp jmeter_host_${worker}:${JMETER_REMOTE_FOLDER_NAME}/result/${RESULT_FILE} ${RESULT_FOLDER}/
done
