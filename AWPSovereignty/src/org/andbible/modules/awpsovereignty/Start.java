package org.andbible.modules.awpsovereignty;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.andbible.modules.creation.AddLinks;
import org.andbible.modules.creation.CorrectCapitalization;
import org.andbible.modules.creation.InsertParagraphs;
import org.andbible.modules.creation.TidyCharacters;
import org.apache.commons.io.IOUtils;

public class Start {
	
	public static void main(String[] args) {
		try {
			String in = IOUtils.toString(new FileInputStream("text/in.txt"));
//			in = new InsertSections().filter(in);
			in = new InsertParts().filter(in);
			in = new RemoveSeparatorLines().filter(in);
			in = new InsertParagraphs().filter(in);
			in = new AddLinks().filter(in);
			in = new TidyCharacters().filter(in);
			in = new CorrectCapitalization().filter(in);
			IOUtils.write(in, new FileOutputStream("AWPSovereignty.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
