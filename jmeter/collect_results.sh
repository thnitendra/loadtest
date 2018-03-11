#!/bin/bash

DIR=$(pwd)
JMETER_REMOTE_FOLDER_NAME="jmeter.loadtest"
TOTAL_QPS=$1
JMETER_START_SID=$2
JMETER_END_SID=$3


if [[ -z $TOTAL_QPS ]]; then
  echo "TOTAL_QPS is empty"
  echo "Pass args like -> ./collect_results.sh totalQPS jMeterServerStart jMeterServerEnd"
  exit 1;
fi

if [[ -z $JMETER_START_SID ]]; then
  echo "JMETER_START_SID is empty"
  echo "Pass args like -> ./collect_results.sh totalQPS jMeterServerStart jMeterServerEnd"
  exit 1;
fi

if [[ -z $JMETER_END_SID ]]; then
  echo "JMETER_END_SID is empty"
  echo "Pass args like -> ./collect_results.sh totalQPS jMeterServerStart jMeterServerEnd"
  exit 1;
fi


#Collecting Results
DATETIME=`date "+%Y%m%d%H%M%S"`
RESULT_FOLDER=results_qps${TOTAL_QPS}_${DATETIME}
mkdir -p ${RESULT_FOLDER}
for worker in `seq ${JMETER_START_SID} ${JMETER_END_SID}`; do
  echo "worker is ${worker}"
  RESULT_FILE=${RESULT_FOLDER}_${worker}.tgz
  echo " Result file is ${RESULT_FILE}"
  ssh jmeter_${worker} "cd ${JMETER_REMOTE_FOLDER_NAME}/result;tar -zcpvf ${RESULT_FILE} *jtl"
  scp jmeter_${worker}:${JMETER_REMOTE_FOLDER_NAME}/result/${RESULT_FILE} ${RESULT_FOLDER}/
done
