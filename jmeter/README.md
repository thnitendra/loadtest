1. Checkout loadtest/jmeter to jmeter.loadtest
2. Download jdk1.8.0_101 and unzip to jmeter.loadtest
3. Download apache-jmeter and unzip to jmeter.loadtest
 ** if the jmx cannot be opened in GUI due to some plugin dependecies, you might need to copy those libs separately. For example:
	- https://stackoverflow.com/questions/25759977/conversion-error-when-opening-jmx-file-from-jmeter-2-7-in-jmeter-2-11
	- download jar files from https://jmeter-plugins.org/downloads/file/JMeterPlugins-Standard-1.4.0.zip
	- extract and copy lib/etx/*.jar to jmeter/lib/etx/

4. Zip them to a file
➜  zip -r jmeter.loadtest.zip main.jmx exec.sh csv /usr/local/java/jdk1.8.0_101 apache-jmeter-xxx

5. Upload jmeter.loadtest.zip to Jmeter agent hosts and exec.sh from commander server
➜  ./exec.sh $QPS jmeter.loadtest.zip $TARGET_HOST_START_SID $TARGET_HOST_END_SID main.sh $JMETER_HOST_START_SID $JMETER_HOST_END_SID $JMETER_REMOTE_CAPACITY
