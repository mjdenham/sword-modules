package org.andbible.modules.creation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.PassageKeyFactory;

public class Start {
	
	public static void main(String[] args) {
		try {
			String in = IOUtils.toString(new FileInputStream("in.xml"));
			in = new InsertParagraphs().filter(in);
			in = new AddLinks().filter(in);
			in = new TidyCharacters().filter(in);
			IOUtils.write(in, new FileOutputStream("jesermons.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
