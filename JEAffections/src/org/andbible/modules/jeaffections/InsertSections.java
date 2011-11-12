package org.andbible.modules.jeaffections;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andbible.modules.creation.Roman;

public class InsertSections {
	/** E.g.
     __________________________________________________________________

   I. It is no sign one way or the other, that religious affections are

	 */
	static Pattern partHeadingPattern = Pattern.compile( "\\s*_{10,}\\n"+ 	// line of underscores with space at start
														 "\\n"+			// empty line
														 "\\s*([IVXLC]+)\\."  // Section I, II, III, ...
														);
	static String replacement = "\n</p>\n</div>\n<div type=\"chapter\" osisID=\"{0}\">\n<title>{1}</title>\n<p>";

	// identical part numbers appear in different sections so prefix with partNumber to differentiate
	//TODO work out PartNo more intelligently
	// part 1 has no sections so auto start on part 2 - frig
	private long partNumber = 2;
	private long lastSection = 0;

	public String filter(String in) {
		Matcher m = partHeadingPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String section = m.group(1);
			long sectionNo = Roman.toLong(section);
			if (sectionNo<=lastSection) {
				partNumber++;
			}
			lastSection = sectionNo;
			String osisId = partNumber+"-"+sectionNo;

			System.out.println("Section "+osisId);
			
			String newText = MessageFormat.format(replacement, "Section "+osisId, "Section "+osisId);
			System.out.println(newText);
			
			m.appendReplacement(retVal,  newText);
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
}
