package org.andbible.modules.creation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertParagraphs {
	static Pattern blankLinePattern = Pattern.compile( "\\n\\s*\\n");
	static String replacement = "\n\t\t\t\t</p><p>\n";
	
	static Pattern removeChars = Pattern.compile("\\s");

	public String filter(String in) {
		Matcher m = blankLinePattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			// replace empty line
			m.appendReplacement(retVal,  replacement);
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
}
