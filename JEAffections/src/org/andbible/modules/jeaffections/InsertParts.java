package org.andbible.modules.jeaffections;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andbible.modules.creation.Roman;

public class InsertParts {
	static Pattern partHeadingPattern = Pattern.compile( "\\s*_{10,}\\n"+ 	// line of underscores with space at start
														 "\\n"+			// empty line
														 "(?:\\s*[A-Z ]+.\\n"+	// title
														 "\\n)?"+			// empty line
														 "\\s*PART\\s*([1-9IV]+).\\n"+ // PART X
														 "\\n"+			// empty line
														 "\\s*([A-Z ,\\t\\n]+).\\n"	// chapter title
														);
	static String replacement = "\n</p>\n</div>\n<div type=\"chapter\" osisID=\"{0}\">\n<title>{1}</title>\n<p>";
	
	public String filter(String in) {
		Matcher m = partHeadingPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String part = m.group(1);
			String title = m.group(2);
			long partNo = Roman.toLong(part);
			// replace empty line
			System.out.println("Part:"+partNo+" Title:"+title);
			
			String newText = MessageFormat.format(replacement, "Part "+partNo+" "+title, title);
			System.out.println(newText);
			
			m.appendReplacement(retVal,  newText);
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
}
