package org.andbible.modules.isv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.crosswire.jsword.versification.BibleBook;

/**
 * convert simple text to Sword Gen Book
 * 
 * Example from ESV
 * 
<verse osisID='Phil.1.3'/>
<title subType="x-preverse" type="section">Thanksgiving and Prayer</title>
<note n="e" osisID="Phil.1.3!crossReference.e" osisRef="Phil.1.3" type="crossReference">See <reference osisRef="Rom.1.8">Rom. 1:8</reference></note>
I thank my God 
<note n="f" osisID="Phil.1.3!crossReference.f" osisRef="Phil.1.3" type="crossReference">
	<reference osisRef="Rom.1.9">Rom. 1:9</reference>; 
	<reference osisRef="Eph.1.16">Eph. 1:16</reference>; 
	<reference osisRef="2Tim.1.3">2 Tim. 1:3</reference>
</note>
in all my remembrance of you,

 * 
 * @author denha1m
 * 
 */

/**
 * TODO
 * the first title is blank and must be removed
 *      <title subType="x-preverse" type="section"></title>
 
 * @author denha1m
 *
 */
public class Start {

	private static final String CR = System.getProperty("line.separator");
	private static final String OSIS_BIBLE_START =
	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
	"<osis xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"+
		"xmlns=\"http://www.bibletechnologies.net/2003/OSIS/namespace\"\n"+
		"xmlns:osis=\"http://www.bibletechnologies.net/2003/OSIS/namespace\"\n"+
		"xsi:schemaLocation=\"http://www.bibletechnologies.net/2003/OSIS/namespace http://www.bibletechnologies.net/osisCore.2.1.1.xsd\">\n"+
		"<osisText osisIDWork=\"ISV\" osisRefWork=\"bible\" xml:lang=\"en\" canonical=\"true\">\n"+
			"<header>\n"+
				"<work osisWork=\"ISV\">\n"+
					"<title>International Standard Version</title>\n"+
					"<identifier type=\"OSIS\">Bible.ISV</identifier>\n"+
					"<scope>Gen-Rev</scope>\n"+
					"<refSystem>Bible.ISV</refSystem>\n"+
				"</work>\n"+
				"<work osisWork=\"defaultReferenceScheme\">\n"+
					"<refSystem>Bible.KJV</refSystem>\n"+
				"</work>\n"+
			"</header>\n";
	private static final String OSIS_BEFORE_NT = "";
	private static final String OSIS_BIBLE_END =
				"</osisText>"+
			"</osis>";

	private BibleBook mCurrentBook;
	private int mCurrentChapter = 0;
	private int mCurrentVerseNo = 0;
	private boolean isVerseEnded = true;
	
	private static final String BEFORE_BOOK = "<w:pStyle w:val=\"Heading3\" />";
	private static final String AFTER_BOOK = "</wx:sect>";

	private static final String OSIS_CHAPTER = "<chapter osisID='%1$s.%2$d'>";
	private static final String OSIS_CHAPTER_END = "</chapter>";
	private static final String BEFORE_CHAPTER_NO = "<w:pStyle w:val=\"Heading4\" />";
	
	private static final String OSIS_SECTION_TITLE_START = "<title type=\"section\">";
	private static final String OSIS_SECTION_TITLE_END = "</title>";
	private static final String BEFORE_SECTION_TITLE = "<w:pStyle w:val=\"Heading5\" />";
	private static final String END = "</w:p>";

	private static final String OSIS_PARAGRAPH_START = "<p>";
	private static final String OSIS_PARAGRAPH_END = "</p>";
	private static final String BEFORE_PARAGRAPH_1 = "<w:p wsp:rsidR=";
	private static final String BEFORE_PARAGRAPH_2 = " wsp:rsidRPr=";
	private boolean isParagraphClosed = true;
	private boolean isNewParagraphRequired = false;
	
	private static final String OSIS_VERSE = "<verse osisID=\"%1$s.%2$d.%3$d\" sID=\"%1$s.%2$d.%3$d\"/>";
	private static final String OSIS_VERSE_END = "<verse eID=\"%1$s.%2$d.%3$d\"/>";
	private static final String BEFORE_VERSE_NO = "<w:rStyle w:val=\"Verse\" />";

	private static final String BEFORE_VERSE = "<w:r wsp:rsidRPr=";
	
	private static final String BEFORE_FOOTNOTE = "<w:footnote>";

	private static final String IGNORE_1 = "<w:sectPr wsp:rsidR=\"";
	private static final String IGNORE_1_END = "</w:sectPr>";

	// mising out the opening '<' allows teh text to be spread over 2 lines e.g. 2Kgs.7.12
	private static final String TEXT = "w:t>";

	public static void main(String[] args) {
		new Start().doit();
	}
	
	private void doit() {
		try {
			System.out.println("Starting");
			String osis = process(new File("xml/ISV-tidied-edited.xml"));
			IOUtils.write(osis, new FileOutputStream("ISV.xml"));
			System.out.println("Finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String process(File aFile) {
		// ...checks on aFile are elided
		StringBuilder out = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				
				out.append(OSIS_BIBLE_START);
				int i = 0;
				while ((line = input.readLine()) != null) {
					i++;
					if (handleUnusualLine(line, input, out)) {
						// handled above
					} else if (line.contains(BEFORE_BOOK)) {
						parseBook(input, out);
					} else if (line.contains(AFTER_BOOK)) {
						endBook(input, out);
					} else if (line.contains(BEFORE_CHAPTER_NO)) {
						parseChapterNo(input, out);
					} else if (line.contains(BEFORE_SECTION_TITLE)) {
						parseSectionTitle(input, out);
					} else if (line.contains(BEFORE_PARAGRAPH_1) && line.contains(BEFORE_PARAGRAPH_2)) {
						newParagraph(out);
					} else if (line.contains(BEFORE_VERSE_NO)) {
						parseVerseNo(input, out);
//					} else if (line.contains(BEFORE_VERSE)) {
//						parseVerse(input, out);
					} else if (line.contains(BEFORE_FOOTNOTE)) {
						parseFootnote(input, out);
					} else if (line.contains(IGNORE_1)) {
						ignore(input, IGNORE_1_END);
					} else {
						printIfText(line, out);
					}
					
				}
				out.append(OSIS_BIBLE_END);
			} finally {
				input.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return out.toString();
	}

	private void printIfText(String line, StringBuilder out) throws IOException {
		if (line.contains(TEXT)) {
			String text = fixUpText(trimTags(line));
			out.append(text);
//			// joining all text together sometimes merges words so add a space
//			// Israeli is often split from final s - Israelis
//			if (!text.endsWith("Israeli")) {
//				out.append(" ");
//			}
		}
	}

	private boolean handleUnusualLine(String line, BufferedReader input, StringBuilder out) throws Exception {
		
		if (line.contains("<w:t>36:1</w:t>")) {
			// Exodus 36:1 is weird
			checkVerseEnded(out);
			checkParagraphEnded(out);
			newChapter(out, 36);
			newVerse(out, 1);
			return true;
		}
		return false;
	}

	private void parseBook(BufferedReader input, StringBuilder out) throws Exception {
		out.append(CR);
		BibleBook book = null;
		String bookName = "";
		String line = null;
		do {
			line = input.readLine();
			if (line.contains(TEXT) && book==null) {
				bookName = mapBookNo(fixUpText(trimTags(line)));
				book = BibleBook.getBook(bookName);
				System.out.println(bookName);
			}
		} while (!line.contains(END) || book==null);

		mCurrentBook = book;
		mCurrentChapter = 1;
		mCurrentVerseNo = 1;

		String osisBookName = book.getOSIS();
		
		out.append("<div type=\"book\" osisID=\""+osisBookName+"\" canonical=\"true\">");
		out.append(CR);

		// start the first chapter - required for single chapter books
		out.append(CR);
		out.append(String.format(OSIS_CHAPTER, mCurrentBook, mCurrentChapter));
	}
	
	private void endBook(BufferedReader input, StringBuilder out) throws IOException {
		checkVerseEnded(out);
		checkParagraphEnded(out);
		input.readLine();
		out.append(OSIS_CHAPTER_END);
		out.append("</div>");
		out.append(CR);
		if (mCurrentBook.equals(BibleBook.MAL)) {
			out.append(OSIS_BEFORE_NT);
			// skip pre-amble because it confuses
			skipNTPreamble(input);
		}
	}
	
	private void skipNTPreamble(BufferedReader input) throws IOException {
		String line = null;
		do {
			line = input.readLine();

		} while (!line.contains(AFTER_BOOK));
	}
	
	private void parseChapterNo(BufferedReader input, StringBuilder out) throws IOException {
		checkVerseEnded(out);
		checkParagraphEnded(out);
		
		int chapterNo = -1;
		String line = null;
		String text = "";
		do {
			line = input.readLine();

			if (line.contains(BEFORE_FOOTNOTE)) {
				parseFootnote(input, out);
			} else if (line.contains(TEXT)) {
				text += fixUpText(trimTags(line)).trim();
			}
		} while (!line.contains(END) || text.isEmpty());

		text = StringUtils.removeStart(text, "Chapter").trim();
		text = StringUtils.removeStart(text, "Psalm").trim();
		try {
			chapterNo = Integer.valueOf(text);
		} catch (Exception e) {
			System.out.println("Err:Text in place of chapter:"+text);
		}

		if (chapterNo!=mCurrentChapter) {
			newChapter(out, chapterNo);
		}
	}

	private void newChapter(StringBuilder out, int chapterNo) {
		mCurrentChapter = chapterNo;
		mCurrentVerseNo = 1;

		if (mCurrentChapter>1) {
			out.append(CR);
			out.append(OSIS_CHAPTER_END);
		}
		
		out.append(CR);
		out.append(String.format(OSIS_CHAPTER, mCurrentBook, mCurrentChapter));
	}

	private void parseSectionTitle(BufferedReader input, StringBuilder out) throws IOException {
		out.append(CR);
		out.append(OSIS_SECTION_TITLE_START);
		String line = null;
		do {
			line = input.readLine();
			if (line.contains(TEXT)) {
				out.append(fixUpText(trimTags(line)));
			}
		} while (!line.contains(END));

		out.append(OSIS_SECTION_TITLE_END);
	}
	
	private void newParagraph(StringBuilder out) {
		// it will be added at the earliest reasonable place
		isNewParagraphRequired = true;
	}
	
	private void parseVerseNo(BufferedReader input, StringBuilder out) throws Exception {
		checkVerseEnded(out);
		
		out.append(CR);
		int verseNo = -1;
		String line = null;
		do {
			line = input.readLine();
			if (handleUnusualLine(line, input, out)) {
				return;
			}
			if (line.contains(TEXT)) {
				try {
					String verse = trimTags(line).trim();
					verseNo = Integer.valueOf(verse);
				} catch (Exception e) {
					System.out.println("Warning:Text in place of verse:"+line);
					printIfText(line, out);
				}
			}
		} while (verseNo==-1);

		newVerse(out, verseNo);
	}

	private void newVerse(StringBuilder out, int verseNo) {
		mCurrentVerseNo = verseNo;
		out.append(makeVerseStartTag());
		isVerseEnded = false;
	}
	
//	private void parseVerse(BufferedReader input, StringBuilder out) throws IOException {
//		String line = null;
//		do {
//			line = input.readLine();
//			if (line.contains(TEXT)) {
//				out.append(trimTags(fixUpText(line)));
//			} else if (line.contains(BEFORE_FOOTNOTE)) {
//				parseFootnote(input, out);
//			}
//		} while (!line.contains(END));
//
//		out.append(CR);
//	}

	private void parseFootnote(BufferedReader input, StringBuilder out) throws IOException {
		String line = null;
		do {
			line = input.readLine();
// Ignore Footnotes initiallly
//			if (line.contains(TEXT)) {
//				out.append(trimTags(line));
//			}
		} while (!line.contains(END));

//		out.append(System.getProperty("line.separator"));
	}

	private void ignore(BufferedReader input, String until) throws IOException {
		String line = null;
		do {
			line = input.readLine();
		} while (!line.contains(until));
	}

	private String trimTags(String text) {
		text = text.trim();
		int start = 0;
		if (text.startsWith("<")) {
			start = text.indexOf(">")+1;
		}

		int end = text.length();
		if (text.endsWith(">")) {
			end = text.lastIndexOf("<");
		}
		
		if (start<end) {
			return text.substring(start, end);
		} else {
			return "";
		}
	}

	private String mapBookNo(String book) {
		return book.replace("First", "1")
				.replace("Second", "2")
				.replace("Third", "3");
	}
	private String fixUpText(String text) {
		String fixedUp = text
							.replace("&#226;&#8364;&#732;", "'")	// open quoting apostrophe e.g. Gen 3:23 shall be called '(<-this apostrophe)Woman'
							.replace("&#226;&#8364;&#8482;", "'")	// apostrophe
							.replace("&#226;&#8364;&#8221;", " - ") // hyphen e.g. 2 occurrences in Gen 1:16
							.replace("&#226;&#8364;&#8220;", "-") 	// hyphen in a verse range e.g. Ex 1:1-4
							.replace("&#195;&#173;", "i")	// i acute e.g. in last chapter of Ezra v38: Baní, Binai, Shihezi
							.replace("&#195;&#175;", "i")	// accented i as in naive 
							.replace("&#195;&#169;", "e")	// accented e as in last e of naivite 
							.replace("&#226;&#8364;&#339;", "<q marker='\"' />") // open quote
							.replace("&#226;&#8364;", "<q marker='\"' />")		// close quote
							.replace("&#166;", "...") // ellipses e.g. Gen 18:31
							.replace("&#239;&#187;&#191;", "") // this doesn't seem to be anything
							.replace("&#194; ", "") // this doesn't seem to be anything
							 ;
		return fixedUp;
	}
	private String makeVerseStartTag() {
		return String.format(OSIS_VERSE, mCurrentBook.getOSIS(), mCurrentChapter, mCurrentVerseNo);
	}
	private void checkIfParagraphRequired(StringBuilder out) {
		if (isNewParagraphRequired && mCurrentBook!=null && mCurrentChapter>0 && mCurrentVerseNo>0) {
			checkParagraphEnded(out);
			out.append(OSIS_PARAGRAPH_START);
			isNewParagraphRequired = false;
			isParagraphClosed = false;
		}
	}
	private void checkParagraphEnded(StringBuilder out) {
		if (!isParagraphClosed && mCurrentBook!=null && mCurrentChapter>0 && mCurrentVerseNo>0) {
			out.append(OSIS_PARAGRAPH_END).append(CR);
			isParagraphClosed = true;
		}
	}
	private void checkVerseEnded(StringBuilder out) {
		if (!isVerseEnded && mCurrentBook!=null && mCurrentChapter>0 && mCurrentVerseNo>0) {
			out.append(String.format(OSIS_VERSE_END, mCurrentBook.getOSIS(), mCurrentChapter, mCurrentVerseNo));
			isVerseEnded = true;
		}
		
		checkIfParagraphRequired(out);
	}
}
