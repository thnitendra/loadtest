#!/bin/bash
./gradlew clean gatling -Dgatling-noReports=true -Psim=simulations.Main -PjvmArgs='-Dduration=10 -Dtestcase=testcase/basic.json -Dendpoints=http://samples.openweathermap.org'

