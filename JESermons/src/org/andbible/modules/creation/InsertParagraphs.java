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
		
		int unmatchedStartPos = 0;

		while (m.find()) {
			int start = m.start();
			int end = m.end();

			// append text between matches
			retVal.append(in.substring(unmatchedStartPos, start));
			// replace empty line
			retVal.append(replacement);

			unmatchedStartPos = end;
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		if (unmatchedStartPos<in.length()) {
			retVal.append(in.substring(unmatchedStartPos));
		}

		return retVal.toString();
	}
}
