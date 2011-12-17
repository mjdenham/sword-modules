package org.andbible.modules.creation;

public class OSISHelper {
	/** convert any string into a valid OSIS Id
	 */
	public static String getValidOsisId(String text) {
		return text .replace(",", "")
					.replace(".", "")
					.replace(" - ", " ")
					.replace(" -- ", " ")
					.replace("'", "")
					.replace("?", "")
					.replace("\"", "")
					.replace(";", "")
					.replaceAll("<reference.*>(.*)</reference>", "$1");

	}
}
