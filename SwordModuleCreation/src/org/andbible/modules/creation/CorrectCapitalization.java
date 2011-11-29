package org.andbible.modules.creation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;

public class CorrectCapitalization {
	static Pattern capitalsPattern = Pattern.compile( "([A-Z][A-Z \t']{3,100}[A-Z])|THE |ONE |SIN |IN |IT |DO |IF ");
	
	public String filter(String in) {
		Matcher m = capitalsPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String capitals = m.group(0);
			String mixedCase =WordUtils.capitalizeFully(capitals);
			
			System.out.println("Capitalized:"+capitals+" TO:"+mixedCase);
			
			m.appendReplacement(retVal, mixedCase );
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
}
