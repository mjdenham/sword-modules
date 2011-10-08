package org.andbible.modules.creation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.PassageKeyFactory;

public class ConvertRomanNumbersToArabic {
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
		
		while (m.find()) {
			String ref = m.group(0);
			// remove tabs and CRs because they confuse PassageKeyFactory
			String tidiedRef = removeChars.matcher(ref).replaceAll(" ");
			
			// check if the part that matched the regexp is a valid bible reference
			Key key = PassageKeyFactory.instance().getValidKey(tidiedRef); 
			if (!key.isEmpty()) {
				// found a proper passage reference
				String link = "<reference osisRef=\""+key.getOsisID()+"\">"+key.getName()+"</reference>";
				System.out.println(tidiedRef+"->"+link);
				m.appendReplacement(retVal, link);
			}
		}
		
		// append any trailing space after the last match, or if no match then the whole string
		m.appendTail(retVal);

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
