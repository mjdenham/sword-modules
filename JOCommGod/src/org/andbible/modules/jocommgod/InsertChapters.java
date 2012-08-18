package org.andbible.modules.jocommgod;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andbible.modules.creation.OSISHelper;
import org.andbible.modules.creation.OldeEnglishModerniser;
import org.andbible.modules.creation.Roman;

public class InsertChapters {
	/** E.g.
     __________________________________________________________________

   I. It is no sign one way or the other, that religious affections are

	 */
	static Pattern partHeadingPattern = Pattern.compile( "\\s*_{10,}\\n"+ 	// line of underscores with space at start
														 "\\n"+			// empty line
														 "\\s*Chapter ([IVXLC]+)[\\.]?"+  // Chapter I, II, III, ...
														 "\\n"			// empty line
														);
	static String replacement = "\n</p>\n</div>\n<div type=\"chapter\" osisID=\"{0}\">\n<title>{1}</title>\n<p>";

	// identical part numbers appear in different sections so prefix with partNumber to differentiate
	//TODO work out PartNo more intelligently
	// part 1 has no sections so auto start on part 2 - frig
	private long partNumber = 1;
	private long lastChapter = 0;

	public String filter(String in) {
		Matcher m = partHeadingPattern.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			long chapterNo = Roman.toLong(m.group(1));
			if (chapterNo<lastChapter) {
				partNumber++;
			}
			
			String chapter = "Chapter "+chapterNo;
			String part = "Part "+partNumber;
			// modernize here to prevent addition of <note/> in OSISId and title later 
			String title = part+" "+chapter;
			String osisId = OSISHelper.getValidOsisId(title);

			System.out.println("Section "+osisId);
			
			String newText = MessageFormat.format(replacement, osisId, title);
			
			m.appendReplacement(retVal,  newText);
			
			lastChapter = chapterNo;
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

		return retVal.toString();
	}
}
