package org.andbible.modules.creation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.PassageKeyFactory;

public class AddLinks {
	// Deuteronomy 32:35
	// Psalm 73:18,19
	// 1 Corinthians 1:29, 30, 31
	// Isa. 57:20
	// Eccles. 2:16
	// Isa.
    // 33:12-14  // split over 2 lines
	// Matt. 11:25-27
	static Pattern patt = Pattern.compile( "(?:(?:[123]|I{1,3})\\s*)?(?:[A-Z][a-zA-Z]+|Song of Songs|Song of Solomon).?\\s*(?:1?[0-9]?[0-9]):\\s*\\d{1,3}(?:[,-]\\s*\\d{1,3})*(?:;\\s*(?:(?:[123]|I{1,3})\\s*)?(?:[A-Z][a-zA-Z]+|Song of Songs|Song of Solomon)?.?\\s*(?:1?[0-9]?[0-9]):\\s*\\d{1,3}(?:[,-]\\s*\\d{1,3})*)*"); //".*see ([HEBREW|GREEK]) for (\\d{1,5}).*");
	
	static Pattern removeChars = Pattern.compile("\\s");

	public String filter(String in) {
		Matcher m = patt.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		int unmatchedStartPos = 0;

		while (m.find()) {
			int start = m.start();
			int end = m.end();

			// append text between matches
			retVal.append(in.substring(unmatchedStartPos, start));

			String ref = m.group(0);
			// remove tabs and CRs because they confuse PassageKeyFactory
			String tidiedRef = removeChars.matcher(ref).replaceAll(" ");
			
			// check if the part that matched the regexp is a valid bible reference
			Key key = PassageKeyFactory.instance().getValidKey(tidiedRef); 
			if (key.isEmpty()) {
				// not a proper passage reference so just append the text
				retVal.append(ref);
			} else {
				// valid ref found
				String link = "<reference osisRef=\""+key.getOsisID()+"\">"+key.getName()+"</reference>";
				System.out.println(tidiedRef+"->"+link);
				retVal.append(link);
//				// we removed all tabs and CRs so add one at end if there was one in original ref
//				if (ref.contains("\n")) {
//					retVal.append("\n");
//				}
			}

			unmatchedStartPos = end;
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		if (unmatchedStartPos<in.length()) {
			retVal.append(in.substring(unmatchedStartPos));
		}

		String out;
		return retVal.toString();
	}
	
//	private void init() {
//		
//		String books;
//    	for (BibleBook book: EnumSet.range(BibleBook.GEN, BibleBook.REV)) {
//    		try {
//    			books += book.getLongName()+"|"+book.getShortName();
//    			buttonInfo.id = book.ordinal();
//	    		buttonInfo.name = getShortBookName(book, isShortBookNamesAvailable);
//	    		buttonInfo.textColor = getBookTextColor(book.ordinal());
//    		} catch (NoSuchVerseException nsve) {
//    			buttonInfo.name = "ERR";
//    		}
//
//		
//		
//	}
	
	

}
