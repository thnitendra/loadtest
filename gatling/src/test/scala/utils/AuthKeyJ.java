package utils;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public  class AuthKeyJ {



	private static String getNow() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }




	 public static String testPostAuthSuccess(String requestUri, String requestType, String requestBody, String gpId, String issueSecretKey) throws Exception {
	        // Step1: Create payload
	        byte[] hmacData = null;
	        String HEADER_SEPARATOR = ";";
	        String transactionTime = getNow();
	        System.out.println("transactionTime = " + transactionTime);
	        String payload = requestUri + transactionTime + requestBody;
	        //logger.info("The payload is " + payload);



	        // step2:generate signature
	        SecretKeySpec secretKey = new SecretKeySpec(issueSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	        Mac mac = Mac.getInstance("HmacSHA256");
	        mac.init(secretKey);
	        hmacData = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
	        String signature = new String(Base64.getEncoder().encode(hmacData));
	        //logger.info("The signature is " + signature);


	        // step3:Base64 encode
	        String authzHeader = gpId + HEADER_SEPARATOR + signature + HEADER_SEPARATOR + transactionTime;
	        System.out.println("The authzHeader is " + authzHeader);
	        return new String(Base64.getEncoder().encode(authzHeader.getBytes()), StandardCharsets.UTF_8);


	 }

	 public static void main(String args[] ) throws Exception{


		 System.out.println(testPostAuthSuccess("http://pms-api-qa.cloudapp.net:8080/grant/standard/64fd0ad9-8250-4795-9267-568c2554afa5","POST","{\"gpId\":2,\"expenseId\":101,\"requestKey\":\"20160412064\",\"points\":100}","2","testsecretkey"));

	 }
}
