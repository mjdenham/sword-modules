package org.andbible.modules.jochrist;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andbible.modules.creation.OSISHelper;
import org.andbible.modules.creation.OldeEnglishModerniser;
import org.apache.commons.lang.StringUtils;

public class InsertParts {
	static Pattern partHeadingPattern = Pattern.compile( "\\s*_{10,}\\n"+ 	// line of underscores with space at start
														 "\\n"+			// empty line
														 "\\s*([A-Za-z0-9\":;\'. ]+)\\n"	// title
														);
	static String replacement = "\n</p>\n</div>\n<div type=\"chapter\" osisID=\"{0}\">\n<title>{1}</title>\n<p>";
	
	public String filter(String in) {
		Matcher m = partHeadingPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String title = m.group(1).replace("\n", " - ");
			title = StringUtils.removeEnd(title, " - ");

			// modernize here to prevent addition of <note/> in OSISId and title later 
			title = new OldeEnglishModerniser().filter(title,  false);

			String osisId = OSISHelper.getValidOsisId(title);
			// replace empty line
			System.out.println(" Title:"+title);
			
			String newText = MessageFormat.format(replacement, osisId, title);
			
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
