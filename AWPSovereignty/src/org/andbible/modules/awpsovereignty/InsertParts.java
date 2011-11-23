package org.andbible.modules.awpsovereignty;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class InsertParts {
	static Pattern partHeadingPattern = Pattern.compile( "\\s*_{10,}\\n"+ 	// line of underscores with space at start
														 "\\n"+			// empty line
														 "\\s*([A-Z0-9\":\' \\n]+)\\n"	// title
														);
	static String replacement = "\n</p>\n</div>\n<div type=\"chapter\" osisID=\"{0}\">\n<title>{1}</title>\n<p>";
	
	public String filter(String in) {
		Matcher m = partHeadingPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String title = m.group(1).replace("\n", " - ");
			title = StringUtils.removeEnd(title, " - ");
			String safeTitle = title.replace(",", "").replace(" - ", " ").replace("'", "").replace("\"", "").replaceAll("<reference.*>(.*)</reference>", "$1");
			// replace empty line
			System.out.println(" Title:"+title);
			
			String newText = MessageFormat.format(replacement, safeTitle, title);
			System.out.println(newText);
			
			m.appendReplacement(retVal,  newText);
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
	
	public static final void main(String[] args) {
		System.out.println("Appendix 3 The Meaning Of Kosmos In <reference osisRef=\"John.3.16\">John 3:16</reference>".replaceAll("<reference.*>(.*)</reference>", "$1"));
	}
}
