package org.andbible.modules.creation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.PassageKeyFactory;

public class AddLinks {
	// reg exp derived from http://regexlib.com/REDetails.aspx?regexp_id=2288
	// Deuteronomy 32:35
	// Psalm 73:18,19
	// 1 Corinthians 1:29, 30, 31
	// Isa. 57:20
	// Eccles. 2:16
	// Isa.
    // 33:12-14  // split over 2 lines
	// Matt. 11:25-27
	static Pattern bibleRefPattern = Pattern.compile( "(?:(?:[123]|I{1,3}\\.?)\\s*)?"+  // number preceding book name e.g. 1 Cor, 2 Kings, 3 John
											"(?:[A-Z][a-zA-Z]+|Song of Songs|Song of Solomon).?\\s+"+ // book
											"((?:(?:1?[0-9]?[0-9])|(?:[clxviCLXVI]{1,7})))[:.]\\s*"+ // chapter using arabic or roman numerals
											"\\d{1,3}(?:[,-]\\s*\\d{1,3})*"+ // verse
											"[.]?"+  // roman verse refs generally have a dot after the verse
											// adding the below caters for references like II Ki. 2:11; 3:12-22
											"(?:;\\s*"+							// I am not sure what the ; is doing here, but it precedes a possible repeated ref
											"(?:(?:[123]|I{1,3})\\s*)?(?:1?[0-9]?[0-9]):\\s*\\d{1,3}(?:[,-]\\s*\\d{1,3})*)*");
	
	static Pattern removeChars = Pattern.compile("\\s");

	public String filter(String in) {
		Matcher m = bibleRefPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		int unmatchedStartPos = 0;
		while (m.find()) {
			int matchStart = m.start();
			int matchEnd = m.end();
			
			// append text between matches
			retVal.append(in.substring(unmatchedStartPos, matchStart));

			String ref = m.group(0);
			String chapter = m.group(1);

			// looks like roman numerals - so convert to arabic numerals
			String alteredRef = ref;
			if (StringUtils.containsOnly(chapter.toLowerCase(), "clxvi")) {
				String arabic = String.valueOf(Roman.toLong(chapter));
				alteredRef = in.substring(matchStart, m.start(1))+arabic+in.substring(m.end(1), matchEnd);
			}
			
			retVal.append( processPossibleReference(alteredRef, ref) );

			unmatchedStartPos = matchEnd;
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		if (unmatchedStartPos<in.length()) {
			retVal.append(in.substring(unmatchedStartPos));
		}

		return retVal.toString();
	}
	
	private String processPossibleReference(String ref, String unalteredRef) {
		String linkOrOrig = "";
		// remove tabs and CRs because they confuse PassageKeyFactory
		String tidiedRef = removeChars.matcher(ref).replaceAll(" ");
		
		// check if the part that matched the regexp is a valid bible reference
		Key key = PassageKeyFactory.instance().getValidKey(tidiedRef); 
		if (key.isEmpty()) {
			// not a proper passage reference so just append the text
			System.out.println("NOT matched;"+unalteredRef);
			linkOrOrig = unalteredRef;
		} else {
			// valid ref found
			String link = "<reference osisRef=\""+key.getOsisID()+"\">"+key.getName()+"</reference>";
			System.out.println(unalteredRef+"->"+link);
			linkOrOrig = link;
//				// we removed all tabs and CRs so add one at end if there was one in original ref
//				if (ref.contains("\n")) {
//					retVal.append("\n");
//				}
		}
		return linkOrOrig;
	}
}
