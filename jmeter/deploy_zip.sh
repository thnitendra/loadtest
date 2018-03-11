#!/bin/bash

DIR=$(pwd)
JMETER_REMOTE_FOLDER_NAME="jmeter.loadtest"
JMETER_ZIP_FILE=$1
JMETER_START_SID=$2
JMETER_END_SID=$3

if [[ -z $JMETER_ZIP_FILE ]]; then
  echo "zipfile is empty"
  echo "pass arguments like -> ./deploy zipfile jMeterServerStart jMeterServerEnd"
  exit 1;
fi

if [[ -z $JMETER_REMOTE_FOLDER_NAME ]]; then
  echo "JMETER_REMOTE_FOLDER_NAME is empty"
  exit 1;
fi

if [[ -z $JMETER_START_SID ]]; then
  echo "JMETER_START_SID is empty"
  echo "pass arguments like -> ./deploy zipfile jMeterServerStart jMeterServerEnd"
  exit 1;
fi

if [[ -z $JMETER_END_SID ]]; then
  echo "JMETER_END_SID is empty"
  echo "pass arguments like -> ./deploy zipfile jMeterServerStart jMeterServerEnd"
  exit 1;
fi


echo "Deploying"
##Deploy script and jmx files
for host in $(seq ${JMETER_START_SID} ${JMETER_END_SID});do
    echo "deloying ${host}"
    ssh jmeter_${host} "mkdir -p ~/${JMETER_REMOTE_FOLDER_NAME}"
    scp ${JMETER_ZIP_FILE} jmeter_${host}:~/${JMETER_REMOTE_FOLDER_NAME}
    ssh jmeter_${host} "cd ~/${JMETER_REMOTE_FOLDER_NAME};unzip -oq $(basename ${JMETER_ZIP_FILE});rm -f $(basename ${JMETER_ZIP_FILE})"
done

