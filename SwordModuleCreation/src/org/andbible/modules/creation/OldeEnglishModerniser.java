package org.andbible.modules.creation;

import java.util.ArrayList;
import java.util.List;

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
	
	private static List<OldAndModern> oldAndModernWordList;
	
	static {
		oldAndModernWordList = new ArrayList<OldAndModern>();
		oldAndModernWordList.add(new OldAndModern("howbeit", "nevertheless"));
		oldAndModernWordList.add(new OldAndModern("saith", "says"));
		oldAndModernWordList.add(new OldAndModern("doth", "does"));
		oldAndModernWordList.add(new OldAndModern("herein", "in this"));
		oldAndModernWordList.add(new OldAndModern("hereof", "of this"));
		oldAndModernWordList.add(new OldAndModern("hereunto", "to this"));
		oldAndModernWordList.add(new OldAndModern("therein", "in that"));
		oldAndModernWordList.add(new OldAndModern("thereof", "of that"));
		oldAndModernWordList.add(new OldAndModern("thereon", "upon that"));
		oldAndModernWordList.add(new OldAndModern("wherein", "in which"));
		oldAndModernWordList.add(new OldAndModern("whereof", "of which"));
		oldAndModernWordList.add(new OldAndModern("wherewith", "with which"));
		oldAndModernWordList.add(new OldAndModern("wherefore", "therefore"));
		oldAndModernWordList.add(new OldAndModern("whereunto", "to which"));
		oldAndModernWordList.add(new OldAndModern("whereby", "through which"));
		oldAndModernWordList.add(new OldAndModern("unto", "to"));
		oldAndModernWordList.add(new OldAndModern("hath", "has"));
		
		oldAndModernWordList.add(new OldAndModern("whence", "from where"));
		oldAndModernWordList.add(new OldAndModern("from from", "", "from")); // whence is sometimes already preceded by 'from' so remove 'from from' caused by previous replacement
		
		oldAndModernWordList.add(new OldAndModern("conjunction", "union"));
		
		String[] REMOVE_ETH_ADD_S = new String[] {
				"claspeth","knoweth","worketh","soaketh","belongeth","showeth","manifesteth","comprehendeth","profiteth","exhibiteth","dwelleth","inhabiteth",
				"asketh","thirsteth","longeth","existeth","designeth","looketh","differeth","consisteth","aboundeth","groaneth","standeth","panteth","subsisteth",
				"proceedeth","bringeth","bloweth","listeth","aimeth","heareth","discerneth","healeth","redeemeth","crowneth","expecteth","findeth"
			};
		for (String old : REMOVE_ETH_ADD_S) {
			oldAndModernWordList.add(new OldAndModern(old, old.substring(0, old.length()-3)+"s"));
		}

		String[] REMOVE_ETH_ADD_ES = new String[] {
				"goeth","cometh","presseth","urgeth","perplexeth","requireth","rejoiceth","perisheth","dieth","lieth","receiveth","conduceth","taketh","ariseth",
				"passeth","maketh","shineth","expresseth","reproacheth","loveth","liveth","abideth","humbleth","causeth","giveth","loseth","testifieth","becometh",
				"carrieth","reduceth","ceaseth","pleaseth","sanctifieth","ravisheth","communicateth","teacheth","proposeth","hideth","opposeth","refresheth","riseth",
				"extinguisheth","proposeth","opposeth","compriseth","promoteth","seduceth","supplieth","wisheth","refuseth","increaseth","surpriseth","intermeddleth",
				"forgiveth","satisfieth","enlargeth","flourisheth","loatheth","promiseth","includeth","changeth"
			};
		for (String old : REMOVE_ETH_ADD_ES) {
			oldAndModernWordList.add(new OldAndModern(old, old.substring(0, old.length()-3)+"es"));
		}

		oldAndModernWordList.add(new OldAndModern("sitteth", "sits"));
	}

	public String filter(String in, boolean addNote) {
		
		for (OldAndModern oldAndModern : oldAndModernWordList) {
			String old = oldAndModern.old;
			String modern = oldAndModern.modern;
			String noteWord = oldAndModern.note;
			String note = addNote && StringUtils.isNotEmpty(noteWord) ? "<note n=\""+noteWord+"\">Auto-modernized</note>" : "";
			in = in.replaceAll("\\b"+old+"\\b", modern+note);
			in = in.replaceAll("\\b"+StringUtils.capitalize(old)+"\\b", StringUtils.capitalize(modern)+note);
		}
		
		
		// there a lot of double hyphens that confuse TTS but don't remove them from xml comments
		return in;
	}
	
	private static class OldAndModern{
		private String old;
		private String modern;
		private String note;

		public OldAndModern(String old, String modern) {
			this.old = old;
			this.modern = modern;
			this.note = old;
		}
		public OldAndModern(String old, String note, String modern) {
			this.old = old;
			this.note = note;
			this.modern = modern;
		}
	}	
}
