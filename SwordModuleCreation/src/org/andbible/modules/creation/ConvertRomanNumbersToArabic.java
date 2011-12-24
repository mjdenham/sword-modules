package org.andbible.modules.creation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertRomanNumbersToArabic {
	// Deuteronomy 32:35
	// Psalm 73:18,19
	// 1 Corinthians 1:29, 30, 31
	// Isa. 57:20
	// Eccles. 2:16
	// Isa.
    // 33:12-14  // split over 2 lines
	// Matt. 11:25-27
	private static Pattern patt = Pattern.compile( "\\b([IVXLC]{1,5})\\b");
	
	private static String I = "I"; 
	private static String LXX = "LXX"; 

	public String filter(String in) {
		Matcher m = patt.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		while (m.find()) {
			String roman = m.group(0);

			// ignore I which is a valid English word
			if (!I.equals(roman) && !LXX.equals(roman)) {
				
				String arabic = Long.toString(Roman.toLong(roman));
				
				System.out.println(roman+"->"+arabic);
				m.appendReplacement(retVal, arabic);
			} else {
				m.appendReplacement(retVal, roman);
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
