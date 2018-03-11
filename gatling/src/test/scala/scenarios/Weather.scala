package scenarios

//import io.gatling.core.Predef.{csv, exec, feed, group, scenario}
//import io.gatling.http.Predef.{http, status}
import settings.Props

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
  * Created by nitendra.thakur on 2/14/18.
  */
object Weather {

  val weather = scenario("weather")
    .during(Props.duration second) {
      feed(csv("weather.csv").circular)
        .group("weather") {
          exec(http("lat=${lat}&lon=${lon}")
            .get("/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${appid}")
            //.headers(Props.common_header)
            .check(status.is(200)))
        }
    }
}
