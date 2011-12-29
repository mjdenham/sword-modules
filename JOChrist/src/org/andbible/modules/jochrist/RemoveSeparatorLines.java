package org.andbible.modules.jochrist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * remove remaining separator lines that often occur before Footnotes e.g.
 * 
   labor in vain, Gal. 4:11, 15.
   __________________________________________________________________

  [19] Agreeable to this, Mr. Stoddard observes, in his Guide to Christ,

 * @author denha1m
 *
 */
public class RemoveSeparatorLines {
	static Pattern seperatorPattern = Pattern.compile( "\\s*_{10,}\\n"+ 	// line of underscores with space at start
														 "\\n"			// empty line
														);
	static String replacement = "\n</p>\n<p>";
	
	public String filter(String in) {
		Matcher m = seperatorPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String seperator = m.group(0);
			
			m.appendReplacement(retVal, replacement);
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
}
