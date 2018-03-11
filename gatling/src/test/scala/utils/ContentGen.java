package utils;

import java.util.Random;

public class ContentGen {


	public static String easyIdDataCreation(int number, int randomNumber) {
		StringBuilder lcleasyIdmycontents = new StringBuilder();
		for (int i = 0; i < randomNumber; i++) {
			if (i != 0) {
				lcleasyIdmycontents.append(", ");
			}
			lcleasyIdmycontents.append(number++);
		}
		return lcleasyIdmycontents.toString();

	}

	public static String ipDataCreation(String ipaddr, int randomNumber) {
		StringBuilder lclIpmycontents = new StringBuilder();
		for (int i = 0; i < randomNumber; i++) {
			if (i == 0) {
				lclIpmycontents.append("\"");
			}
			String myNewNumber = splitNApplyIcrLogic(ipaddr);
			if (i < randomNumber - 1)
				lclIpmycontents.append(myNewNumber + "\", \"");
			else
				lclIpmycontents.append(myNewNumber + "\"");
		}
		return lclIpmycontents.toString();

	}

	public static String splitNApplyIcrLogic(String number) {
		String[] output = number.split("\\.");

		int fourthElement = Integer.parseInt(output[3]);
		int thirdElement = Integer.parseInt(output[2]);
		int secondElement = Integer.parseInt(output[1]);
		int firstElement = Integer.parseInt(output[0]);
		StringBuilder finaloutput = new StringBuilder();

		if (fourthElement == 254) {
			fourthElement = 0;

			if (thirdElement >= 254) {
				thirdElement = 0;

				if (secondElement == 254) {
					secondElement = 0;
					if (firstElement == 254)
						firstElement = 1;
					else
						++firstElement;
				}

				else
					++secondElement;
			} else {
				++thirdElement;

			}
		} else {
			++fourthElement;

		}

		String ipaddr = finaloutput.append(firstElement).append(".").append(secondElement).append(".")
				.append(thirdElement).append(".").append(fourthElement).toString();
		return ipaddr;

	}

	static int getRandomNumberInRange(int min, int max) {
/*if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}*/

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

}
