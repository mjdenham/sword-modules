package org.andbible.modules.isv
import org.apache.commons.lang.StringUtils


class DictionaryCheck {
	val m = scala.collection.mutable.Map[String, Int]()
	
	def add(text:String) {
		for (word:String <- text.split("[\\W]+")) {
			var wordLower = word.toLowerCase()
			var option = m.get(wordLower)
			if (option.isDefined) {
				m.put(wordLower, option.get+1)
			} else {
				m.put(wordLower, 1)
			}
		}
	}
	
	def printUniqueWords() {
		m.filter(pair => pair._2 <= 1).filter(pair => !StringUtils.isNumeric(pair._1)).foreach(println(_))
	}
}