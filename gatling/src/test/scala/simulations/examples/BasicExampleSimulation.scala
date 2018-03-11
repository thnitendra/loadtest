package simulations.examples

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


class BasicExampleSimulation extends Simulation {

  val uri = "http://rakid-dev-api.cloudapp.net/dev"
  val captured_token =""
  val captured_tokenbuilder =""
  val USER=""
  
  
 
  
  val httpProtocol = http
                     .baseURL("http://rakid-dev-api.cloudapp.net/dev")
    

  val headers_0 = Map(
    "Authorization" -> "Basic aWRfdGVzdDAxOmNsaWVudF9jcmVkZW50aWFsc19zZWNyZXQ=",
    "User-Agent" -> "Apache-HttpClient/4.2.6")
    
    
    captured_tokenbuilder.concat("Bearer "+ captured_token.toString())

  val headers_1 = Map(
    "Authorization" -> "${requestVerificationToken}")
    
    val headers_2 = Map(
    "Authorization" -> "Basic aWRfdGVzdDAyOnNlY3JldF9pZF90ZXN0MDI=")
    

    println(captured_token) 
    println(captured_tokenbuilder) 
    println(USER) 

    
   
    
  val scn6 = scenario("MemberExist")
      
      //.during(duration seconds) {
        .feed(csv("queries.csv").circular)
        .exec(http("Get_OAuth_Token_of_client_credentials_01")
        .post(uri + "/gid/auth/token/get")
        .formParamMap(Map("grant_type" -> "client_credentials"))
        .headers(headers_0)
        .check(regex("""access_token":"([^"]*)""")
        .saveAs("requestVerificationToken")))
        .pause(5)
        .exec(http("Check_the_Client Member_ID_Exist_02")
        .post(uri + "/gid/client/client-member-id/exist")
        .headers(Map(
         "Authorization" -> "Bearer ${requestVerificationToken}"))
        
        .formParamMap(Map("grant_type" -> "client_credentials", "client_member_id" -> "${query}"))
        .check(regex("""result":true""")))
        .pause(5)
      
   // }
  
        
         val scn5 = scenario("Memberget")
         pace(20)
        .feed(csv("queries2.csv").circular)
        .exec(http("Get_OAuth_Token_of_password_01")
        .post(uri + "/gid/auth/token/get")
        .formParamMap(Map("grant_type" -> "${Oauth_GRANT_TYPE}", "username" -> "${Oauth_USER}", "password" -> "${Oauth_PASSWORD}", "login_route" -> "${Oauth_LOGIN_ROUTE}"))
        .headers(headers_2)
        .check(regex("""access_token":"([^"]*)""")
        .saveAs("requestVerificationToken1")))
        .pause(5)
        .exec(http("Check_client-member_02")
        .post(uri + "/gid/user/client-member-id/get")
        .headers(Map(
         "Authorization" -> "Bearer ${requestVerificationToken1}",
         "X-Client-UserAgent" -> "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36",
         "X-Client-IP" -> "127.9.0.1"))
        .check(regex("""member_id":""")))
        .pause(5)
  
  setUp(scn5
      .inject(rampUsers(Integer.getInteger("users", 1)) over 5.seconds)
     
    )
    
   /* setUp(
  scn5.inject(rampUsers(10) over (10 seconds)),
  scn6.inject(rampUsers(2) over (10 seconds))
).maxDuration(5 minutes)
       */
}