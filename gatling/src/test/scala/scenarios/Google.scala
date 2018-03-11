package scenarios

//import io.gatling.core.Predef.{csv, exec, feed, scenario}
//import io.gatling.http.Predef.{http, status}
import settings.Props

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
  * Created by nitendra.thakur on 2/14/18.
  */
object Google {

  val google = scenario("google")
    .during(Props.duration second) {
      feed(csv("google.csv").circular)
        .group("google") {
          exec(http("${enpoint_url}")
            .get("${enpoint_url}")
            //.headers(Props.common_header)
            .check(status.is(200))
          )
        }
    }
}
