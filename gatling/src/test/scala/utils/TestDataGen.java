package utils;

import java.util.HashMap;
import java.util.UUID;

public class TestDataGen {

	public static String getipGenRandomly() {

		StringBuilder finaloutput = new StringBuilder();

		int myRndNumber1 = ContentGen.getRandomNumberInRange(1, 254);
		int myRndNumber2 = ContentGen.getRandomNumberInRange(1, 254);
		int myRndNumber3 = ContentGen.getRandomNumberInRange(1, 254);
		int myRndNumber4 = ContentGen.getRandomNumberInRange(1, 254);

		String ipaddr = finaloutput.append(myRndNumber1).append(".").append(myRndNumber2).append(".")
				.append(myRndNumber3).append(".").append(myRndNumber4).toString();

		return ipaddr;

	}

	public static String getEasyIdGenRandomly() {

		return Integer.toString(ContentGen.getRandomNumberInRange(1, 1000000000));

	}

	public static String getSidIdGenRandomly() {

		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();

		return randomUUIDString;

	}

	public static String getipGenRandomly(String ipaddress) {

		StringBuilder finaloutput = new StringBuilder();

		int myRndNumber1 = ContentGen.getRandomNumberInRange(1, 254);
		int myRndNumber2 = ContentGen.getRandomNumberInRange(1, 254);
		int myRndNumber3 = ContentGen.getRandomNumberInRange(1, 254);
		int myRndNumber4 = ContentGen.getRandomNumberInRange(1, 254);

		String ipaddr = finaloutput.append(myRndNumber1).append(".").append(myRndNumber2).append(".")
				.append(myRndNumber3).append(".").append(myRndNumber4).toString();

		return ipaddr;

	}

	public static String getEasyIdGenRandomly(String easyId1,String easyId2 ) {

		return Integer.toString(ContentGen.getRandomNumberInRange( Integer.parseInt(easyId1.toString()), Integer.parseInt(easyId2.toString())));

	}

	public static String getipGenRandomly(String ipaddress1, String ipaddress2) {

		StringBuilder finaloutput = new StringBuilder();
		String[] startrange = ipaddress1.split("\\.");
		String[] endrange = ipaddress2.split("\\.");
		int myRndNumber1 = ContentGen.getRandomNumberInRange(toint(startrange[0]), toint(endrange[0]));
		int myRndNumber2 = ContentGen.getRandomNumberInRange(toint(startrange[1]), toint(endrange[1]));
		int myRndNumber3 = ContentGen.getRandomNumberInRange(toint(startrange[2]), toint(endrange[2]));
		int myRndNumber4 = ContentGen.getRandomNumberInRange(toint(startrange[3]), toint(endrange[3]));

		String ipaddr = finaloutput.append(myRndNumber1).append(".").append(myRndNumber2).append(".")
				.append(myRndNumber3).append(".").append(myRndNumber4).toString();

		/*
		 * System.out.println(myRndNumber1); System.out.println(myRndNumber2);
		 * System.out.println(myRndNumber3); System.out.println(myRndNumber4);
		 */

		return ipaddr;
	}


	public static HashMap<String, String> randomStringPicker() {
		String range[] = new String[4];
		HashMap<String, String> retrurnIpEasyId = new HashMap<String, String>();

		int myRndNumber1 = ContentGen.getRandomNumberInRange(0, CommonData.ipaddressesrange.length-1);
		String[] forsplittedip = CommonData.ipaddressesrange[myRndNumber1].split("-");
		String[] forsplittedeasyid = CommonData.easyIdrange[myRndNumber1].split("-");
		range[0] = forsplittedip[0];
		range[1] = forsplittedip[1];
		range[2] = forsplittedeasyid[0];
		range[3] = forsplittedeasyid[1];

		retrurnIpEasyId.put("ipaddress1", forsplittedip[0]);
		retrurnIpEasyId.put("ipaddress2", forsplittedip[1]);
		retrurnIpEasyId.put("easyId1", forsplittedeasyid[0]);
		retrurnIpEasyId.put("easyId2", forsplittedeasyid[1]);

		return retrurnIpEasyId;
	}
	public static int toint(String numberAsString) {

		return Integer.parseInt(numberAsString);
	}




}
