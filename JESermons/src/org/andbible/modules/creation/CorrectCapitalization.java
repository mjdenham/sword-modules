package org.andbible.modules.creation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;

public class CorrectCapitalization {
	static Pattern capitalsPattern = Pattern.compile( "([A-Z][A-Z \t']{3,100}[A-Z])|THE");
	
	public String filter(String in) {
		Matcher m = capitalsPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		int unmatchedStartPos = 0;
		while (m.find()) {
			int matchStart = m.start();
			int matchEnd = m.end();
			
			// append text between matches
			retVal.append(in.substring(unmatchedStartPos, matchStart));

			String capitals = m.group(0);
			String mixedCase =WordUtils.capitalizeFully(capitals);
			
			System.out.println("Capitalized:"+capitals+" TO:"+mixedCase);
			
			retVal.append( mixedCase );

			unmatchedStartPos = matchEnd;
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		if (unmatchedStartPos<in.length()) {
			retVal.append(in.substring(unmatchedStartPos));
		}

		return retVal.toString();
	}
}
