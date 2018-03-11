#bin/bash!

SERVER_START=$1
SERVER_END=$2

if [[ -z $SERVER_START ]]; then
  SERVER_START=101
  echo "SERVER_START=101"
fi

if [[ -z $SERVER_END ]]; then
  SERVER_END=130
  echo "SERVER_END=130"
fi


for NUM in `seq $SERVER_START $SERVER_END`; do
    echo "================= kill all java processs in jmeter_${HOST}"
    ssh jmeter_${HOST} "pkill -f java"
done

