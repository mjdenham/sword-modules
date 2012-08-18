package org.andbible.modules.jocommgod;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andbible.modules.creation.OSISHelper;
import org.andbible.modules.creation.OldeEnglishModerniser;
import org.andbible.modules.creation.Roman;

public class InsertInitialChapters {
	/** E.g.
     __________________________________________________________________

   I. It is no sign one way or the other, that religious affections are

	 */
	static Pattern partHeadingPattern = Pattern.compile( "\\s*_{10,}\\n"+ 	// line of underscores with space at start
														 "\\n"+			// empty line
														 "\\s*([A-Za-z.,;\\- ]+)\\n"+	// chapter title
														 "\\n"			// empty line
														);
	static String replacement = "\n</p>\n</div>\n<div type=\"chapter\" osisID=\"{0}\">\n<title>{1}</title>\n<p>";

	public String filter(String in) {
		Matcher m = partHeadingPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String title = m.group(1);
			// modernize here to prevent addition of <note/> in OSISId and title later 
			title = new OldeEnglishModerniser().filter(title,  false);
			String osisId = OSISHelper.getValidOsisId(title);

			System.out.println("Initial chapter "+osisId);
			
			String newText = MessageFormat.format(replacement, osisId, title);
			
			m.appendReplacement(retVal,  newText);
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
}
