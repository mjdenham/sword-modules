package org.andbible.modules.isv

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import org.crosswire.jsword.versification.BibleBook
import java.util.ArrayList
import scala.collection.mutable.ListBuffer
import org.andbible.modules.creation.AddLinks
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.KeyUtil

/**
* remove all semi-colons
* remove 'final' before class variables
* add def before methods
* 
* 
* optional
* fields are private so remove private before fields

 */

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
	<reference osisRef="Rom.1.9">Rom. 1:9</reference> 
	<reference osisRef="Eph.1.16">Eph. 1:16</reference> 
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
object SMakeISV {
	def main(args: Array[String]): Unit = {
	  new SMakeISV().doit()
	}
}

class SMakeISV {

	val quick = true
	
	val CR = System.getProperty("line.separator")
	val OSIS_BIBLE_START =
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
			"</header>\n"
	val OSIS_BEFORE_NT = ""
	val OSIS_BIBLE_END =
				"</osisText>"+
			"</osis>"

	var mCurrentBook:BibleBook = _
	var mCurrentChapter = 0
	var mCurrentVerseNo = 0
	var isVerseEnded = true
	
	val BEFORE_BOOK = "<w:pStyle w:val=\"Heading3\" />"
	val AFTER_BOOK = "</wx:sect>"

	val OSIS_CHAPTER = "<chapter osisID='%1$s.%2$d'>"
	val OSIS_CHAPTER_END = "</chapter>"
	val BEFORE_CHAPTER_NO = "<w:pStyle w:val=\"Heading4\" />"

	var pendingVerseTitles = new ListBuffer[String]
	val OSIS_SECTION_TITLE_START = "<title subType=\"x-preverse\" type=\"section\">";
	val OSIS_SUBSECTION_TITLE_START = "<title subType=\"x-preverse\" type=\"section\" level=\"2\">";
	val OSIS_SECTION_TITLE_END = "</title>"
	val BEFORE_SECTION_TITLE = "<w:pStyle w:val=\"Heading5\" />"
	val BEFORE_RELATED_REF_TITLE = "<w:pStyle w:val=\"Heading6\" />"
	val END = "</w:p>"

	val OSIS_PARAGRAPH_START = "" //"<lb type=\"x-begin-paragraph\"/>"
	val OSIS_PARAGRAPH_END = "<lb/>"+CR //"<lb type=\"x-end-paragraph\"/>"
	val BEFORE_PARAGRAPH_1 = "<w:p wsp:rsidR="
	val BEFORE_PARAGRAPH_2 = " wsp:rsidRPr="
	var isParagraphClosed = true
	var isNewParagraphRequired = false
	
	val OSIS_VERSE = "<verse osisID=\"%1$s.%2$d.%3$d\" sID=\"%1$s.%2$d.%3$d\"/>"
	val OSIS_VERSE_END = "<verse eID=\"%1$s.%2$d.%3$d\"/>"
	val BEFORE_VERSE_NO = "<w:rStyle w:val=\"Verse\" />"

	val BEFORE_VERSE = "<w:r wsp:rsidRPr="
	
	var footnoteNo = 0
	// e.g. "<note n=\"1\" osisID=\"Gen.1.26!note.1\" osisRef=\"Gen.1.26\" type=\"explanation\">"
	// 1= note no; 2=OsisId
	val OSIS_PRE_FOOTNOTE = "<note n=\"%1$d\" osisID=\"%2$s!note.%1$d\" osisRef=\"%2$s\" type=\"explanation\">"
	val OSIS_POST_FOOTNOTE = "</note>"
	val BEFORE_FOOTNOTE = "<w:footnote>"
    val BEFORE_FOOTNOTE_TEXT = "<w:pStyle w:val=\"FootnoteText\" />"

    	
	var pendingPoetryStartTags = ""
    var isInPoetry = false
	val OSIS_PRE_PSALM_LINES = "<lg>"
	val OSIS_POST_PSALM_LINES = "</lg>"
	val OSIS_PRE_PSALM_LINE_1 = "<l level='1'>"
	val OSIS_PRE_PSALM_LINE_2 = "<l level='2'>"
	val OSIS_PRE_PSALM_LINE_3 = "<l level='3'>"
	val OSIS_POST_PSALM_LINE = "</l>"
	
//	val OSIS_PRE_PSALM_LINE = "<milestone type=\"x-extra-p\"/>"
	val BEFORE_PSALM_LINE_ANY = "PsalmLine"
//	val BEFORE_PSALM_LINE_1 = "<w:pStyle w:val=\"PsalmLine1\" />"
//	val BEFORE_PSALM_LINE_2 = "<w:pStyle w:val=\"PsalmLine2\" />"
//	val BEFORE_PSALM_LINE_1_CONTINUED = "<w:pStyle w:val=\"PsalmLine1Continued\" />"
//	val BEFORE_PSALM_LINE_2_LAST_LINE = "<w:pStyle w:val=\"PsalmLine2LastLine\" />"
//	val BEFORE_PSALM_LINE_3_LAST_LINE = "<w:pStyle w:val=\"PsalmLine3LastLine\" />"
//	val AFTER_PSALM_LINES = "<w:pStyle w:val=\"NormalContinued\" />"
		
		/*
                  <w:pStyle w:val="StylePsalmLine110pt" />
                  <w:pStyle w:val="StylePsalmLine210pt" />

		 */

	val SMALLCAPS = "<w:smallCaps />"
	var isWOC = false
//	val OSIS_PRE_WOC = "<q marker=\"\" who=\"Jesus\">"
//	val OSIS_POST_WOC = "</q>"
	val BEFORE_WOC_RED = "<w:color w:val=\"FF0000\" />"
	
	val IGNORE_1 = "<w:sectPr wsp:rsidR=\""
	val IGNORE_1_END = "</w:sectPr>"

	// mising out the opening '<' allows teh text to be spread over 2 lines e.g. 2Kgs.7.12
	val TEXT = "w:t>"
		
	var currentQuotesID:Int = 1

	def doit(): Unit = {
		try {
			println("Starting")
			var osis = process(new File("xml/ISV-tidied-edited.xml"))
			IOUtils.write(osis, new FileOutputStream("ISV.xml"))
			println("Finished")
		} catch {
		  case e:Exception => e.printStackTrace()
		}
	}

	private def process(aFile: File):String = {
		// ...checks on aFile are elided
		var resultStr = ""
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			var input = new BufferedReader(new FileReader(aFile))
			try {
				var out = new StringBuilder()
				var line:String = null // not declared within while loop
				
				out.append(OSIS_BIBLE_START)
				while ({line = input.readLine(); line != null}) {
					if (handleUnusualLine(line, input, out)) {
						// handled above
					} else if (line.contains(BEFORE_BOOK)) {
						parseBook(input, out)
					} else if (line.contains(AFTER_BOOK)) {
						endBook(input, out)
					} else if (line.contains(BEFORE_CHAPTER_NO)) {
						parseChapterNo(input, out)
					} else if (line.contains(BEFORE_SECTION_TITLE)) {
						parseSectionTitle(input, out)
					} else if (line.contains(BEFORE_RELATED_REF_TITLE)) {
						parseRelatedRefTitle(input, out)
					} else if (line.contains(BEFORE_PARAGRAPH_1) && line.contains(BEFORE_PARAGRAPH_2)) {
						// poetry tags include new lines in them
						if (!isInPoetry) {
							newParagraph(out)
						}
					} else if (line.contains(BEFORE_VERSE_NO)) {
						parseVerseNo(input, out)
					} else if (line.contains(BEFORE_PSALM_LINE_ANY)) {
						startPoetryLine(input, out, line)
//					} else if (line.contains(BEFORE_VERSE)) {
//						parseVerse(input, out)
					} else if (line.contains(BEFORE_FOOTNOTE) || line.contains(BEFORE_FOOTNOTE_TEXT)) {
						parseFootnote(input, out)
// ignore WOC for now because too many errors in q nesting					
					} else if (line.contains(BEFORE_WOC_RED)) {
						isWOC = true
					} else if (line.contains(END)) {
						isWOC = false
					} else if (line.contains(SMALLCAPS)) {
						// leave as a placeholder for replacement later after all text has been merged
						out.append(SMALLCAPS)
					} else if (line.contains(IGNORE_1)) {
						ignore(input, IGNORE_1_END)
					} else {
						// write any delayed tags before the actual text is written
						checkIfParagraphRequired(out)
						flushPendingPoetryStartTags(out)
						
						printIfText(line, out)
					}
					
				}
				out.append(OSIS_BIBLE_END)
				
				resultStr = out.toString()
				resultStr = resultStr.replace(SMALLCAPS+"Lord", " Lord ")
									  .replace("L"+SMALLCAPS+"ord", " Lord ")
									  .replace(SMALLCAPS+"God", " God ")
									  .replace("G"+SMALLCAPS+"od", " God ")
									  .replace(SMALLCAPS+"LORD", "<divineName>Lord</divineName>")
									  .replace("L"+SMALLCAPS+"ORD", "<divineName>Lord</divineName>")
									  .replace(SMALLCAPS, "")
				// I don't know why there are duplicated line breaks throughout
				for (i <- 1 to 3) {
					resultStr = resultStr.replace(OSIS_PARAGRAPH_END+OSIS_PARAGRAPH_END, OSIS_PARAGRAPH_END)
				}
			} finally {
				input.close()
			}
		} catch {
		  case e:Exception => e.printStackTrace()
		}

		return resultStr
	}

	private def printIfText(line:String, out:StringBuilder) = {
		if (line.contains(TEXT)) {
			var text = fixUpText(trimTags(line))
			out.append(text)

//			// joining all text together sometimes merges words so add a space
//			out.append(" ")
		}
	}

	private def handleUnusualLine(line:String, input:BufferedReader, out:StringBuilder):Boolean = {
		
		if (line.contains("<w:t>36:1</w:t>")) {
			// Exodus 36:1 is weird
			checkVerseEnded(out)
			checkParagraphEnded(out)
			newChapter(out, 36)
			newVerse(out, 1)
			return true
		}
		return false
	}

	private def parseBook(input:BufferedReader, out:StringBuilder) = {
		out.append(CR)
		var book:BibleBook = null
		var bookName = ""
		var line:String = null
		do {
			line = input.readLine()
			if (line.contains(TEXT) && book==null) {
				bookName = mapBookNo(fixUpText(trimTags(line)))
				book = BibleBook.getBook(bookName)
				println(bookName)
			}
		} while (!line.contains(END) || book==null)

		mCurrentBook = book
		mCurrentChapter = 1
		mCurrentVerseNo = 1

		var osisBookName = book.getOSIS()
		
		out.append("<div type=\"book\" osisID=\""+osisBookName+"\" canonical=\"true\">")
		out.append(CR)

		// start the first chapter - required for single chapter books
		out.append(CR)
		out.append(OSIS_CHAPTER.format( mCurrentBook, mCurrentChapter))
	}
	
	private def endBook(input:BufferedReader, out:StringBuilder) = {
		checkVerseEnded(out)
		checkParagraphEnded(out)
		input.readLine()
		out.append(OSIS_CHAPTER_END)
		out.append("</div>")
		out.append(CR)
		if (mCurrentBook.equals(BibleBook.MAL)) {
			out.append(OSIS_BEFORE_NT)
			// skip pre-amble because it confuses
			skipNTPreamble(input)
		}
	}
	
	private def skipNTPreamble(input:BufferedReader) = {
		var line:String = null
		do {
			line = input.readLine()

		} while (!line.contains(AFTER_BOOK))
	}
	
	private def parseChapterNo(input:BufferedReader, out:StringBuilder) = {
		checkVerseEnded(out)
		checkParagraphEnded(out)
		
		// reset note counter after every chapter
		footnoteNo = 0
		
		var chapterNo:Int = -1
		var line:String = null
		var text:String = ""
		do {
			line = input.readLine()

			if (line.contains(BEFORE_FOOTNOTE)) {
				parseFootnote(input, out)
			} else if (line.contains(TEXT)) {
				text += fixUpText(trimTags(line)).trim()
			}
		} while (!line.contains(END) || text.isEmpty())

		text = StringUtils.removeStart(text, "Chapter").trim()
		text = StringUtils.removeStart(text, "Psalm").trim()
		try {
			chapterNo = Integer.valueOf(text)
		} catch {
		  case e:Exception => println("Err:Text in place of chapter:"+text)
		}

		if (chapterNo!=mCurrentChapter) {
			newChapter(out, chapterNo)
		}
	}

	private def newChapter(out:StringBuilder, chapterNo:Int) = {
		// poetry lines aren't always closed before chapters but should be
		checkPoetryLinesClosed(out)
		
		mCurrentChapter = chapterNo
		mCurrentVerseNo = 1

		if (mCurrentChapter>1) {
			out.append(CR)
			out.append(OSIS_CHAPTER_END)
		}
		
		out.append(CR)
		out.append(OSIS_CHAPTER.format(mCurrentBook, mCurrentChapter))
		
		//reset quoteID counter
//		currentQuotesID = 1;
	}

	private def parseSectionTitle(input:BufferedReader, out:StringBuilder) = {
		val title = new StringBuilder
		var nonEmptyTitle = false
		title.append(CR)
		title.append(OSIS_SECTION_TITLE_START)
		var line:String = null
		do {
			line = input.readLine()
			if (line.contains(TEXT)) {
				title.append(fixUpText(trimTags(line)))
				nonEmptyTitle = true
			}
		} while (!line.contains(END))

		title.append(OSIS_SECTION_TITLE_END)
		
		if (nonEmptyTitle) {
			pendingVerseTitles+=title.toString()
		}
	}
	private def parseRelatedRefTitle(input:BufferedReader, out:StringBuilder) = {
		val title = new StringBuilder
		var nonEmptyTitle = false
		title.append(CR)
		title.append(OSIS_SUBSECTION_TITLE_START)
		var line:String = null
		do {
			line = input.readLine()
			if (line.contains(TEXT)) {
				title.append(fixUpText(trimTags(line)))
				nonEmptyTitle = true
			}
		} while (!line.contains(END))

		title.append(OSIS_SECTION_TITLE_END)
		
		var titleStr:String = ""
		if (!quick) {
			titleStr = 	new AddLinks().filter(title.toString())
		} else {
			titleStr = title.toString()
		}

		if (nonEmptyTitle) {
			pendingVerseTitles+=titleStr
		}
	}
	
	private def newParagraph(out:StringBuilder) {
		// it will be added at the earliest reasonable place
		isNewParagraphRequired = true
	}
	
	private def parseVerseNo(input:BufferedReader, out:StringBuilder):Unit = {
		checkVerseEnded(out)
		
		out.append(CR)
		var verseNo:Int = -1
		var line:String = null
		do {
			line = input.readLine()
			if (handleUnusualLine(line, input, out)) {
				return
			}
			if (line.contains(TEXT)) {
			  
				try {
					var verse = trimTags(line).trim()
					verseNo = Integer.valueOf(verse)
				} catch {
				  case e:Exception => {
				 	  println("Warning:Text in place of verse:"+line)
				 	  printIfText(line, out);
				  }
				}
			}
		} while (verseNo == -1)

		newVerse(out, verseNo)

		// append titles
		pendingVerseTitles.foreach( title => out.append(title) )
		pendingVerseTitles.clear()
	}

	private def newVerse(out:StringBuilder, verseNo:Int) {
		mCurrentVerseNo = verseNo
		out.append(makeVerseStartTag())
		isVerseEnded = false
	}
	
//	private def parseVerse(input:BufferedReader, out:StringBuilder) throws IOException {
//		line:String = null
//		do {
//			line = input.readLine()
//			if (line.contains(TEXT)) {
//				out.append(trimTags(fixUpText(line)))
//			} else if (line.contains(BEFORE_FOOTNOTE)) {
//				parseFootnote(input, out)
//			}
//		} while (!line.contains(END))
//
//		out.append(CR)
//	}

	private def parseFootnote(input:BufferedReader, out:StringBuilder) = {
		var line:String = null
		footnoteNo = footnoteNo+1
		out.append(OSIS_PRE_FOOTNOTE.format(footnoteNo, getVerseOSISId))
		do {
			line = input.readLine()
			if (line.contains(TEXT)) {
				out.append(trimTags(line))
			}
		} while (!line.contains(END))
		out.append(OSIS_POST_FOOTNOTE)
		out.append(CR)
	}
	
	private def getVerseOSISId:String = {
		try {
			return new Verse(mCurrentBook, mCurrentChapter, mCurrentVerseNo).getOsisID()
		} catch {
		  case e:Exception => {
			println("Error generating note "+mCurrentBook+":"+mCurrentChapter+":"+mCurrentVerseNo)
			return mCurrentBook.getOSIS()+"."+mCurrentChapter+"."+mCurrentVerseNo
		  }
		}
	}
	
//	val BEFORE_PSALM_LINE_1 = "<w:pStyle w:val=\"PsalmLine1\" />"
//	val BEFORE_PSALM_LINE_2 = "<w:pStyle w:val=\"PsalmLine2\" />"
//	val BEFORE_PSALM_LINE_1_CONTINUED = "<w:pStyle w:val=\"PsalmLine1Continued\" />"
//	val BEFORE_PSALM_LINE_2_LAST_LINE = "<w:pStyle w:val=\"PsalmLine2LastLine\" />"
//	val BEFORE_PSALM_LINE_3_LAST_LINE = "<w:pStyle w:val=\"PsalmLine3LastLine\" />"
//	val AFTER_PSALM_LINES = "<w:pStyle w:val=\"NormalContinued\" />"
		/*
                  <w:pStyle w:val="StylePsalmLine110pt" />
                  <w:pStyle w:val="StylePsalmLine210pt" />
		 */
	var currentPoetryLevel = 0
	private def startPoetryLine(input:BufferedReader, out:StringBuilder, line:String) = {
		// delay writing tags to avoid putting opening <lg><l> in previous verse
		flushPendingPoetryStartTags(out)
		
		var levelStr = line.charAt(line.indexOf("PsalmLine")+9).toString()
		var indentationLevel = Integer.parseInt(levelStr);
		var isLastLine = line.contains("LastLine");
		
		if (currentPoetryLevel!=indentationLevel || pendingPoetryStartTags.isEmpty()) {
			if (currentPoetryLevel>0) {
				// terminate previous level
				out.append(OSIS_POST_PSALM_LINE)
			} else {
				isInPoetry = true
				// often the first line of poetry is on a new line 
				checkIfParagraphRequired(out)
				// starting first psalm line so need to add psalm lines wrapper tag
				pendingPoetryStartTags += OSIS_PRE_PSALM_LINES
			}
			
			var prePsalmLine = ""
			indentationLevel match {
				case 1 => pendingPoetryStartTags += OSIS_PRE_PSALM_LINE_1
				case 2 => pendingPoetryStartTags += OSIS_PRE_PSALM_LINE_2
				case 3 => pendingPoetryStartTags += OSIS_PRE_PSALM_LINE_3
			}

			// poetry lines are on a new line anyway
			isNewParagraphRequired = false
			isParagraphClosed = true
		}

		currentPoetryLevel = indentationLevel

		// last line so parse poetry line text and then close poetry section
		if (isLastLine) {
			flushPendingPoetryStartTags(out)
			var verseLine = ""
			while ({verseLine = input.readLine(); !verseLine.contains(END)}) {
	
				if (verseLine.contains(BEFORE_FOOTNOTE)) {
					parseFootnote(input, out)
				} else if (verseLine.contains(BEFORE_VERSE_NO)) {
					// verse in last line in Job 41:10
					parseVerseNo(input, out)
				} else if (verseLine.contains(TEXT)) {
					out.append( fixUpText(trimTags(verseLine)).trim() )
				}
			}
			checkPoetryLinesClosed(out)

			// since text is consumed to END need to check end of WOC because END is stop marker for WOC
			isWOC = false
		}
	}
	
	private def flushPendingPoetryStartTags(out:StringBuilder) {
		out.append(pendingPoetryStartTags)
		pendingPoetryStartTags = ""
	}
	
	private def checkPoetryLinesClosed(out:StringBuilder) {
		isInPoetry = false
		if (currentPoetryLevel>0) {
			out.append(OSIS_POST_PSALM_LINE)
			out.append(OSIS_POST_PSALM_LINES)
		}
		currentPoetryLevel = 0
	}

	private def ignore(input:BufferedReader, until:String) = {
		var line:String = null
		do {
			line = input.readLine()
		} while (!line.contains(until))
	}

	private def trimTags(textIn:String):String = {
		var addWordseparatorAtEnd = false;
		var text = textIn.trim()
		var start = 0
		if (text.startsWith("<")) {
			start = text.indexOf(">")+1
		}

		var end = text.length()
		if (text.endsWith(">")) {
			end = text.lastIndexOf("<")
		} else {
			addWordseparatorAtEnd = true;
		}
		
		if (start<end) {
			text = text.substring(start, end)
			if (addWordseparatorAtEnd) {
				text += " "
			}
			return text 
		} else {
			return ""
		}
	}

	private def mapBookNo(book:String):String = {
		return book.replace("First", "1")
				.replace("Second", "2")
				.replace("Third", "3")
	}
	private def fixUpText(text:String):String = {
		var fixedUp = text
							.replace("&#226;&#8364;&#732;", "'")	// open quoting apostrophe e.g. Gen 3:23 shall be called '(<-this apostrophe)Woman'
							.replace("&#226;&#8364;&#8482;", "'")	// matching closing apostrophe for above OR possessive apostrophe e.g. God's judgement
							.replace("&#226;&#8364;&#8221;", " - ") // hyphen e.g. 2 occurrences in Gen 1:16
							.replace("&#226;&#8364;&#8220;", "-") 	// hyphen in a verse range e.g. Ex 1:1-4
							.replace("&#195;&#173;", "i")	// i acute e.g. in last chapter of Ezra v38: Baní, Binai, Shihezi
							.replace("&#195;&#175;", "i")	// accented i as in naive 
							.replace("&#195;&#169;", "e")	// accented e as in last e of naivite 
							.replace("&#166;", "...") // ellipses e.g. Gen 18:31
							.replace("&#239;&#187;&#191;", "") // this doesn't seem to be anything
							.replace("&#194; ", "") // this doesn't seem to be anything

		// 100 okay
		// 1000 okay
		// 1521 okay
		// 1525 crashes
		// 1600 crashes
		// 1700 crashes
		// 2000 crashes
		// 5000 crashes
		// 10000 crashes
//		if (currentQuotesID<10000) {
			fixedUp = fixedUp
							.replace("&#226;&#8364;&#339;", getQ(true)) // open quote
							.replace("&#226;&#8364;", getQ(false))		// close quote
//							.replace("&#226;&#8364;&#339;", "<q marker='\"' sID='"+ getQuoteStartIdAndInc +"'/>") // open quote
//							.replace("&#226;&#8364;", "<q marker='\"' eID='"+getQuoteEndId +"'/>")		// close quote
//		}
		
		return fixedUp
	}
	
	var isOpenQ = false
	var quoteID:Int = 0
	def getQ(opening:Boolean):String = {
		var retVal = ""
		if (opening == isOpenQ ) {
			println("ERROR in Q:"+mCurrentBook+" "+mCurrentChapter+" "+mCurrentVerseNo)
		}
		
		var woc = ""
		if (isWOC) {
			woc =  " who=\"Jesus\""
		}
		
		if (!isOpenQ) {
			isOpenQ = true
			quoteID = quoteID + 1
			retVal = "<q marker='\"' sID='"+quoteID+"' />"
		} else {
			isOpenQ = false
			retVal = "<q marker='\"' eID='"+quoteID+"' />"
		}
		return retVal
	}

	
	private def makeVerseStartTag():String = {
		return OSIS_VERSE.format( mCurrentBook.getOSIS(), mCurrentChapter, mCurrentVerseNo)
	}
	private def checkVerseEnded(out:StringBuilder) {
		checkIfParagraphRequired(out)
		if (!isVerseEnded && mCurrentBook!=null && mCurrentChapter>0 && mCurrentVerseNo>0) {
			out.append(OSIS_VERSE_END.format( mCurrentBook.getOSIS(), mCurrentChapter, mCurrentVerseNo))
			isVerseEnded = true
		}
	}
	private def checkIfParagraphRequired(out:StringBuilder) {
		if (isNewParagraphRequired) {
			if (mCurrentBook!=null && mCurrentChapter>0 && mCurrentVerseNo>0) {
				checkParagraphEnded(out)
				out.append(OSIS_PARAGRAPH_START)
			}
			isNewParagraphRequired = false
			isParagraphClosed = false
		}
	}
	private def checkParagraphEnded(out:StringBuilder) {
		if (!isParagraphClosed) {
			if (mCurrentBook!=null && mCurrentChapter>0 && mCurrentVerseNo>0) {
				out.append(OSIS_PARAGRAPH_END)
			}
			isParagraphClosed = true
		}
	}
//	var isInWOC = false
//	var WOCID = 0
//
//	private def startWOC(out:StringBuilder) {
//		checkWOCClosed(out)
//		WOCID = WOCID + 1
//		out.append(getWOCTag(true))
//		isInWOC = true
//	}
//	private def checkWOCClosed(out:StringBuilder) {
//		if (isInWOC) {
//			isInWOC = false
//			out.append(getWOCTag(false))
//		}
//	}
//	private def getWOCTag(start:Boolean):String = {
//		var retVal = ""
//		if (start) {
//			retVal = "<q marker=\"\" who=\"Jesus\" sID='WOC"+WOCID+"' />"
//		} else {
//			retVal = "<q marker=\"\" who=\"Jesus\" eID='WOC"+WOCID+"' />"
//		}
//		return retVal
//	}
}
