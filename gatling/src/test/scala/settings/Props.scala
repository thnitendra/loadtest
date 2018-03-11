package settings

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.http.Predef._
import org.json.JSONObject
import settings.Props.endpoints

import scala.concurrent.duration._
import scala.reflect.internal.util.StringOps

object Props {

  // Endpoints can either be passed as runtime parameters or through CSVs
  val endpoints = System.getProperty("endpoints", "")

  val testcase = System.getProperty("testcase")
  val duration = Integer.getInteger("duration", 10).toLong

  val _proxyhost = System.getProperty("proxyhost")
  val _proxyport = System.getProperty("proxyport")

  //private val _httpConf = http.baseURL(endpoints)
  private val _httpConf = http.baseURLs(endpoints.split(",").toList)
  def httpConf = {
    if(_proxyhost != null && _proxyport != null) {
      val intPort = Integer.parseInt(_proxyport)
      _httpConf.proxy(Proxy(_proxyhost,intPort).httpsPort(intPort))
    } else {
      _httpConf
    }
  }

  val authToken = System.getProperty("authToken", "IQABM-4o7VaD55t1vn32HrDefMmhK0C1XFZ-RVLYMbixYVrtYWG")
  val _Common_header = Map("Authorization" -> "OAuth2 ${authToken}")
  def common_header = _Common_header

  val nodesCount = System.getProperty("nodesCount", "1").toInt
  val nodeIndex = System.getProperty("nodeIndex", "0").toInt

  def byNode(n: Int): Int  = {
    if (n <= nodesCount ) {
      1
    } else {
      var maxRpsPerNode: Int = n / nodesCount
      var modRps: Int = n % nodesCount
      if (nodeIndex < modRps) {
        maxRpsPerNode + 1
      } else {
        maxRpsPerNode
      }
    }
  }

}
