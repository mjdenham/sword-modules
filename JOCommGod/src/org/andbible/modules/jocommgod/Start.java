package org.andbible.modules.jocommgod;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.andbible.modules.creation.AddLinks;
import org.andbible.modules.creation.ConvertRomanNumbersToArabic;
import org.andbible.modules.creation.CorrectCapitalization;
import org.andbible.modules.creation.InsertParagraphs;
import org.andbible.modules.creation.OldeEnglishModerniser;
import org.andbible.modules.creation.TidyCharacters;
import org.apache.commons.io.IOUtils;

/**
 * convert simple text to Sword Gen Book
 * @author denha1m
 *
 */
public class Start {
	
	public static void main(String[] args) {
		try {
			String in = IOUtils.toString(new FileInputStream("text/in.txt"));
			in = new AddLinks().filter(in);
			in = new InsertChapters().filter(in);
			in = new InsertParts().filter(in);
			in = new InsertInitialChapters().filter(in);
			in = new RemoveSeparatorLines().filter(in);
			in = new InsertParagraphs().filter(in);
			in = new OldeEnglishModerniser().filter(in, true);
			in = new TidyCharacters().filter(in);
			in = new CorrectCapitalization().filter(in);
			IOUtils.write(in, new FileOutputStream("JOCommGod.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
