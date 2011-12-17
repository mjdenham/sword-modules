package org.andbible.modules.creation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

/** Change old english words to a more modern equivlent
 * 
howbeit -> Nevertheless | however
saith -> says
hereof -> of this
whereof -> of which | of what | of whom
thereof -> of that | of this | of it
wherewith -> with what | with which
wherefore -> therefore | for which
unto -> to | about
hath -> has
from whence -> from where
whence -> from where | from which
hereunto -> to this
conjunction -> union
 * @author denha1m
 *
 */
public class OldeEnglishModerniser {
	
	private static Map<String, String> mapOldToModernWord;
	
	static {
		mapOldToModernWord = new HashMap<String, String>();
		mapOldToModernWord.put("howbeit", "nevertheless");
		mapOldToModernWord.put("saith", "says");
		mapOldToModernWord.put("hereof", "of this");
		mapOldToModernWord.put("herein", "in this");
		mapOldToModernWord.put("therein", "in that");
		mapOldToModernWord.put("thereon", "upon that");
		mapOldToModernWord.put("whereof", "of which");
		mapOldToModernWord.put("thereof", "of that");
		mapOldToModernWord.put("wherewith", "with what");
		mapOldToModernWord.put("wherefore", "therefore");
		mapOldToModernWord.put("unto", "to");
		mapOldToModernWord.put("hath", "has");
		mapOldToModernWord.put("from whence", "from where");
		mapOldToModernWord.put("whence", "from where");
		mapOldToModernWord.put("whereunto", "to which");
		mapOldToModernWord.put("hereunto", "to this");
		mapOldToModernWord.put("conjunction", "union");
		
		String[] REMOVE_ETH_ADD_S = new String[] {
				"claspeth","knoweth","worketh","soaketh","belongeth","showeth","manifesteth","comprehendeth","profiteth","exhibiteth","dwelleth","inhabiteth",
				"asketh","thirsteth","longeth","existeth","designeth","looketh","differeth","consisteth","aboundeth","groaneth","standeth","panteth","subsisteth",
				"proceedeth","bringeth","bloweth","listeth","aimeth","heareth","discerneth","healeth","redeemeth","crowneth","expecteth","findeth"
			};
		for (String old : REMOVE_ETH_ADD_S) {
			mapOldToModernWord.put(old, old.substring(0, old.length()-3)+"s");
		}

		String[] REMOVE_ETH_ADD_ES = new String[] {
				"goeth","cometh","presseth","urgeth","perplexeth","requireth","rejoiceth","perisheth","dieth","lieth","receiveth","conduceth","taketh","ariseth",
				"passeth","maketh","shineth","expresseth","reproacheth","loveth","liveth","abideth","humbleth","causeth","giveth","loseth","testifieth","becometh",
				"carrieth","reduceth","ceaseth","pleaseth","sanctifieth","ravisheth","communicateth","teacheth","proposeth","hideth","opposeth","refresheth","riseth",
				"extinguisheth","proposeth","opposeth","compriseth","promoteth","seduceth","supplieth","wisheth","refuseth","increaseth","surpriseth","intermeddleth",
				"forgiveth","satisfieth","enlargeth","flourisheth","loatheth","promiseth","includeth","changeth"
			};
		for (String old : REMOVE_ETH_ADD_ES) {
			mapOldToModernWord.put(old, old.substring(0, old.length()-3)+"es");
		}

		mapOldToModernWord.put("sitteth", "sits");
	}

	public String filter(String in, boolean addNote) {
		
		for (Entry<String, String> oldToModern : mapOldToModernWord.entrySet()) {
			String old = oldToModern.getKey();
			String modern = oldToModern.getValue();
			String note = addNote ? "<note n=\""+old+"\">Auto-modernized</note>" : "";
			in = in.replaceAll("\\b"+old+"\\b", modern+note);
			in = in.replaceAll("\\b"+StringUtils.capitalize(old)+"\\b", StringUtils.capitalize(modern)+note);
		}
		
		
		// there a lot of double hyphens that confuse TTS but don't remove them from xml comments
		return in;
	}
	
	
}
