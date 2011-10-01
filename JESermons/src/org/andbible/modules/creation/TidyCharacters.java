package org.andbible.modules.creation;

/** remove or replace certain characters that spoil TTS or XML format
 * 
 * @author denha1m
 *
 */
public class TidyCharacters {

	String filter(String in) {
		// there a lot of double hyphens that confuse TTS but don't remove them from xml comments
		return in.replaceAll("([^!])--([^>])", "$1-$2")
				 .replaceAll("&c;", "etc.")
				 .replaceAll("&c", "etc.");
	}
}
