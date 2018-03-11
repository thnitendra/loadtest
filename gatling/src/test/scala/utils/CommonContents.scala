/*
*****************************************************************************
Author   :------------------------------Paramveer Singh
Version  :------------------------------1.0
Date     :------------------------------23 June 2016
********************************************************
Functionality
*****************************************************************************
*/
package utils;

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

object CommonContents {

  val _pid = "22dfbcc1-f1e9-4345-b3a4-4d879295e990" ;
  val _jsonbodychi = """{"pid":"""" + _pid + """", "lang": null, "rat": null}"""

  val _rvalue = "aaaaaa"
  val _sid = "c86b74fe-b4dd-4659-a81e-da42445445ad"



  def   jsonbodychi = _jsonbodychi
  def   pid = _pid
  def   rvalue = _rvalue
  def   sid = _sid

}
