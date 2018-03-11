#bin/bash!

#sh /usr/local/Cellar/jmeter/4.0/libexec/bin/jmeter.sh -n -t main.jmx   -l ${2}.jtl   
sh /usr/local/Cellar/jmeter/4.0/libexec/bin/jmeter.sh -n -t main.jmx \
  -e -l report.jtl -o ./report \
  -JQPS=2 \
  -JUSER=1 \
  -JDURATION=10 \
  -JHOST=samples.openweathermap.org \
  -JCSV_PATH=../gatling/data
