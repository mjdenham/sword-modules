package org.andbible.modules.creation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.PassageKeyFactory;

public class TidyCharacters {

	String filter(String in) {
		// there a lot of double hyphens that confuse TTS but don't remove them from xml comments
		return in.replaceAll("^\\!(--)^\\>", "-")
				 .replaceAll("&c;", "etc.");
	}
}
