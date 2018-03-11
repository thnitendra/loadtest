#!/bin/bash
export PATH="$PATH":/usr/local/java/jdk1.6.0_37/bin/
export JAVA_HOME=/usr/local/java/jdk1.6.0_37/
PORT=$1
sh /path/to/apache-jmeter-xx/bin/stoptest.sh $PORT