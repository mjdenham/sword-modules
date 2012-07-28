package org.andbible.modules.creation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private static String[] REMOVE_ETH_ADD_S = new String[] {
			"administereth","beareth","belongeth","claspeth","comforteth","concerneth","constraineth","consumeth","containeth","delighteth","denoteth","fainteth","falleth","followeth",
			"hasteneth","joyeth","knoweth","lighteth","rejecteth","reneweth","resteth","soaketh","showeth","trusteth","worketh","manifesteth","comprehendeth","profiteth","exhibiteth",
			"dwelleth","festereth","fighteth","lusteth","melteth","inhabiteth",
			"asketh","thirsteth","longeth","existeth","designeth","looketh","differeth","consisteth","aboundeth","groaneth","standeth","panteth","subsisteth",
			"proceedeth","bringeth","bloweth","listeth","aimeth","heareth","discerneth","healeth","redeemeth","crowneth","expecteth","findeth",
			"assigneth","calleth","casteth","commendeth","exceedeth","exerteth","faileth","feeleth","honoureth","insisteth","intendeth","keepeth","mindeth",
			"ordereth","prayeth","quickeneth","reigneth","remaineth","speaketh","suffereth","suiteth","understandeth","upholdeth",
		};
	
	private static String[] REMOVE_ETH_ADD_ES = new String[] {
			"amazeth","ariseth","chooseth","cometh","confesseth","continueth","declareth","desireth","dieth","discourseth","distinguisheth","doeth","embraceth","fixeth","goeth",
			"perplexeth","presseth","produceth","professeth","purgeth","rejoiceth","requireth","urgeth","perisheth","lieth","receiveth","conduceth","taketh",
			"passeth","maketh","shineth","expresseth","reproacheth","loveth","liveth","abideth","humbleth","causeth","giveth","loseth","testifieth","becometh",
			"carrieth","reduceth","ceaseth","pleaseth","sanctifieth","ravisheth","communicateth","teacheth","hideth","refresheth","riseth",
			"extinguisheth","proposeth","opposeth","compriseth","promoteth","seduceth","supplieth","wisheth","refuseth","increaseth","surpriseth","intermeddleth",
			"forgiveth","satisfieth","enlargeth","flourisheth","loatheth","promiseth","includeth","changeth","undertaketh",
			"accuseth","acknowledgeth","assumeth","believeth","chargeth","commenceth","deceiveth","denieth","describeth","deserveth","disposeth","engageth","evidenceth",
			"exerciseth","exposeth","insinuateth","judgeth","leaveth","pitieth","placeth","proveth","raiseth","reacheth","searcheth","settleth","sufficeth","supposeth",
		};

	
	static {
		oldAndModernWordList = new ArrayList<OldAndModern>();
		oldAndModernWordList.add(new OldAndModern("howbeit", "nevertheless"));
		oldAndModernWordList.add(new OldAndModern("saith", "says"));
		oldAndModernWordList.add(new OldAndModern("art", "are"));
		oldAndModernWordList.add(new OldAndModern("canst", "can"));
		oldAndModernWordList.add(new OldAndModern("complainest", "complain"));
		oldAndModernWordList.add(new OldAndModern("didst", "did"));
		oldAndModernWordList.add(new OldAndModern("doth", "does"));
		oldAndModernWordList.add(new OldAndModern("fearest", "fear"));
		oldAndModernWordList.add(new OldAndModern("fellest", "fell"));
		oldAndModernWordList.add(new OldAndModern("hadst", "have"));
		oldAndModernWordList.add(new OldAndModern("hast", "have"));
		oldAndModernWordList.add(new OldAndModern("herein", "in this"));
		oldAndModernWordList.add(new OldAndModern("hereof", "of this"));
		oldAndModernWordList.add(new OldAndModern("herewith", "with this"));
		oldAndModernWordList.add(new OldAndModern("hereon", "upon this"));
		oldAndModernWordList.add(new OldAndModern("hereunto", "to this"));
		oldAndModernWordList.add(new OldAndModern("knowest", "know"));
		oldAndModernWordList.add(new OldAndModern("livest", "live"));
		oldAndModernWordList.add(new OldAndModern("mayst", "may"));
		oldAndModernWordList.add(new OldAndModern("needest", "need"));
		oldAndModernWordList.add(new OldAndModern("thee", "you"));
		oldAndModernWordList.add(new OldAndModern("therein", "in that"));
		oldAndModernWordList.add(new OldAndModern("thereof", "of that"));
		oldAndModernWordList.add(new OldAndModern("therewithal", "with that"));
		oldAndModernWordList.add(new OldAndModern("thereon", "upon that"));
		oldAndModernWordList.add(new OldAndModern("thereunto", "to that"));
		oldAndModernWordList.add(new OldAndModern("thereinto", "into that"));
		oldAndModernWordList.add(new OldAndModern("thine", "your"));
		oldAndModernWordList.add(new OldAndModern("thou", "you"));
		oldAndModernWordList.add(new OldAndModern("thy", "your"));
		oldAndModernWordList.add(new OldAndModern("thyself", "yourself"));
		oldAndModernWordList.add(new OldAndModern("wast", "were"));
		oldAndModernWordList.add(new OldAndModern("wherein", "in which"));
		oldAndModernWordList.add(new OldAndModern("whereof", "of which"));
		oldAndModernWordList.add(new OldAndModern("wherewith", "with which"));
		oldAndModernWordList.add(new OldAndModern("wherefore", "therefore"));
		oldAndModernWordList.add(new OldAndModern("whereunto", "to which"));
		oldAndModernWordList.add(new OldAndModern("whereinto", "into which"));
		oldAndModernWordList.add(new OldAndModern("whereby", "through which"));
		oldAndModernWordList.add(new OldAndModern("whilst", "while"));
		oldAndModernWordList.add(new OldAndModern("wilt", "will"));
		oldAndModernWordList.add(new OldAndModern("unto", "to"));
		oldAndModernWordList.add(new OldAndModern("hath", "has"));
		
		oldAndModernWordList.add(new OldAndModern("whence", "from where"));
		oldAndModernWordList.add(new OldAndModern("from from", "", "from")); // whence is sometimes already preceded by 'from' so remove 'from from' caused by previous replacement
		
		oldAndModernWordList.add(new OldAndModern("conjunction", "union"));
		oldAndModernWordList.add(new OldAndModern("concernment", "concern"));
		oldAndModernWordList.add(new OldAndModern("concernments", "concerns"));
		oldAndModernWordList.add(new OldAndModern("intendment", "intention"));
		oldAndModernWordList.add(new OldAndModern("supportment", "support"));
		oldAndModernWordList.add(new OldAndModern("disquietment", "disquiet"));
		oldAndModernWordList.add(new OldAndModern("furnishment", "furnishing"));
		oldAndModernWordList.add(new OldAndModern("relinquishment", "relinquishing"));
		
		oldAndModernWordList.add(new OldAndModern("especial", "special"));
		oldAndModernWordList.add(new OldAndModern("an special", "a special")); // fix up previous replacement

		oldAndModernWordList.add(new OldAndModern("1\\^st", "First"));
		oldAndModernWordList.add(new OldAndModern("2\\^dly", "Secondly"));
		oldAndModernWordList.add(new OldAndModern("3\\^dly", "Thirdly"));
		oldAndModernWordList.add(new OldAndModern("4\\^thly", "Fourthly"));
		oldAndModernWordList.add(new OldAndModern("5\\^thly", "Fifthly"));
		oldAndModernWordList.add(new OldAndModern("6\\^thly", "Sixthly"));
		
		for (String old : REMOVE_ETH_ADD_S) {
			oldAndModernWordList.add(new OldAndModern(old, old.substring(0, old.length()-3)+"s"));
		}

		for (String old : REMOVE_ETH_ADD_ES) {
			oldAndModernWordList.add(new OldAndModern(old, old.substring(0, old.length()-3)+"es"));
		}

		oldAndModernWordList.add(new OldAndModern("committeth", "commits"));
		oldAndModernWordList.add(new OldAndModern("putteth", "puts"));
		oldAndModernWordList.add(new OldAndModern("sinneth", "sins"));
		oldAndModernWordList.add(new OldAndModern("sitteth", "sits"));
		oldAndModernWordList.add(new OldAndModern("worshippeth", "worships"));
	}

	public String filter(String in, boolean addNote) {
		
		for (OldAndModern oldAndModern : oldAndModernWordList) {
			String old = oldAndModern.old;
			String modern = oldAndModern.modern;
			String noteWord = oldAndModern.note;
			String note = addNote && StringUtils.isNotEmpty(noteWord) ? "<note n=\""+noteWord+"\">Auto-modernized</note>" : "";
			in = in.replaceAll("\\b"+old+"\\b", modern+note);
			// if starts with a number then capitalize has no effect and replacement will be duplicated causing errors
			if (!StringUtils.isNumeric(old.substring(0, 1))) {
				in = in.replaceAll("\\b"+StringUtils.capitalize(old)+"\\b", StringUtils.capitalize(modern)+note);
			}
		}

		displayOtherETHs(in);
		
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
	
	private void displayOtherETHs(String in) {
		Pattern patt = Pattern.compile( "\\b([A-Za-z]*eth)\\b");
		Matcher m = patt.matcher(in);
		StringBuffer retVal = new StringBuffer();
		
		// use TreeSet to avoid duplictes and sort
		Set<String> wordETHSet = new TreeSet<String>();
		
		while (m.find()) {
			String wordETH = m.group(0);
			wordETHSet.add(wordETH.toLowerCase());
		}
		wordETHSet.removeAll(Arrays.asList(REMOVE_ETH_ADD_ES));
		wordETHSet.removeAll(Arrays.asList(REMOVE_ETH_ADD_S));
		wordETHSet.remove("committeth");
		wordETHSet.remove("putteth");
		wordETHSet.remove("sinneth");
		wordETHSet.remove("sitteth");
		wordETHSet.remove("worshippeth");
		if (wordETHSet.size()>0) {
			System.out.println("Words ending in eth that have not been modernized");
			for (String wordETH : wordETHSet) {
				System.out.println("\""+wordETH+"\",");
			}
		}
	}

}
