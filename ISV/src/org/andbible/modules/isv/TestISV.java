package org.andbible.modules.isv;

import java.util.EnumSet;

import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.OSISUtil;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.Verse;
import org.crosswire.jsword.versification.BibleBook;
import org.crosswire.jsword.versification.BibleInfo;
import org.junit.Test;

/** this is actually just the same as running emptyvss ISV
 * 
 * @author denha1m
 *
 */
public class TestISV {

	private static String MISSING_IN_SOURCE_DOC = "Gen.27.7, Exod.10.8, Exod.25.2, Lev.1.2, Num.5.31, Num.25.9, Deut.1.35, Deut.1.36, Judg.2.21, Judg.4.20, Judg.5.8, Judg.9.2, Judg.16.31, 2Sam.15.16, 1Kgs.1.46, 1Kgs.2.11, 1Kgs.20.32, 1Kgs.21.18, 2Kgs.1.7, 2Kgs.3.17, 2Kgs.10.13, 2Kgs.22.16, 1Chr.23.4, 2Chr.16.9, 2Chr.29.30, Job.6.9, Ps.135.2, Prov.2.22, Jer.3.5, Jer.49.9, Ezek.4.7, Ezek.21.16, Ezek.24.5, Ezek.24.8, Ezek.34.10, Dan.7.19, Mark.15.28, Luke.17.36, Acts.8.37, Acts.15.34, Acts.24.7, Acts.28.29";
	@Test
	public void testCheckISVVersesExist() {
		int missingCount = 0;
		Book isv = Books.installed().getBook("ISV");
    	for (BibleBook book: EnumSet.range(BibleBook.GEN, BibleBook.REV)) {
    		System.out.println(book);
    		try {
	    		for (int chap=1; chap<=BibleInfo.chaptersInBook(book); chap++ ) {
	    			for (int verse=1; verse <= BibleInfo.versesInChapter(book, chap); verse++) {
				        Key key = isv.getKey(new Verse(book, chap, verse).getOsisID());
				        BookData data = new BookData(isv, key);
				        String plainText = OSISUtil.getCanonicalText(data.getOsisFragment());
				        if (plainText.isEmpty() && !MISSING_IN_SOURCE_DOC.contains(key.getOsisID())) {
				        	missingCount++;
				        	System.out.println("Missing:"+key.getOsisID());
				        }
	    			}
	    		}
    		} catch (Exception e) {
    			System.out.println("missing verse");
    		}
    		
    	}
    	System.out.println("Total missing:"+missingCount);

	}

}
