#!/bin/bash
for host in $(seq 101 130);do
	scp -r jmeter_${host}:/path/to/jmeter.loadtest/result ./jmeter.loadtest
done