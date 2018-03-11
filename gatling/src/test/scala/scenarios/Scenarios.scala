package scenarios;

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import settings._

/**
 * https://gatling.io/docs/2.3/general/scenario/
 */
object Scenarios {

  val dummy = scenario("dummy").exec(session => {
    println("dummy api...")
    session
  })

  val scenarioNames = Map (
    "dummy" -> dummy,
    "google" -> Google.google,
    "weather" -> Weather.weather
  )

}
