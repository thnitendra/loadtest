package utils;

public class BlBodyGen {


	final static String Part1 = "{\"sid\":\"";
	final static String Part2 = "\", \"easyid\":[";
	final static String Part3 = "], \"ip\": [";
	final static String Part4 = "]}";

	public static String cutomBatchCreation(String sid, String easyIdBatch, String ipaddresBatch) {
		StringBuilder mycontents = new StringBuilder();

		mycontents.append(Part1.toString() + sid + Part2.toString() + easyIdBatch + Part3.toString() + ipaddresBatch
				+ Part4.toString());
		return mycontents.toString();

	}

	public static String jsonBodyGen(String sid ,String ipaddr,String easyId) {

		int myRndNumber = ContentGen.getRandomNumberInRange(5, 10);
		String ipDataCreatedbatch = ContentGen.ipDataCreation(ipaddr, myRndNumber);
		String easyIdDataCreatedbatch = ContentGen.easyIdDataCreation(Integer.parseInt(easyId), myRndNumber);
		String jsonBodyGen= cutomBatchCreation(sid, easyIdDataCreatedbatch, ipDataCreatedbatch);
		System.out.println(jsonBodyGen);
		return jsonBodyGen;
	}
}
