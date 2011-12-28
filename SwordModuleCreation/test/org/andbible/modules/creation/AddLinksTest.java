package org.andbible.modules.creation;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class AddLinksTest {

	AddLinks links = new AddLinks();

	@Test
	public void simpleNoLinkTest() {
		assertThat( links.filter("abc def"), equalTo("abc def"));
	}

	@Test
	public void simpleLinkTest() {
		assertThat( links.filter("Deuteronomy 32:35"), equalTo("<reference osisRef=\"Deut.32.35\">Deuteronomy 32:35</reference>"));
		assertThat( links.filter("Start Deuteronomy 32:35 End"), equalTo("Start <reference osisRef=\"Deut.32.35\">Deuteronomy 32:35</reference> End"));
	}
	
	@Test
	public void romanNumeralTest() {
		assertThat( links.filter("Matt. xxv. 21"), equalTo("<reference osisRef=\"Matt.25.21\">Matthew 25:21</reference>"));
		assertThat( links.filter("I. Thessalonians. ii. 3"), equalTo("<reference osisRef=\"1Thess.2.3\">1 Thessalonians 2:3</reference>"));
		assertThat( links.filter("And Canticles ii. 15. \"The foxes"), equalTo("And <reference osisRef=\"Song.2.15\">Song of Solomon 2:15</reference> \"The foxes"));
	}

	@Test
	public void chapVerTest() {
		assertThat( links.filter("1\n   Peter. chap. ii. ver. 6-8"), equalTo("<reference osisRef=\"1Pet.2.6 1Pet.2.7 1Pet.2.8\">1 Peter 2:6-8</reference>"));
	}
	
	@Test
	public void chapterTest() {
		assertThat( links.filter("Isaiah chap. vi"), equalTo("<reference osisRef=\"Isa.6\">Isaiah 6</reference>"));
	}

	@Test
	public void confusingTextTest() {
		assertThat( links.filter("1 2 3 Bad 2:3 asdd"), equalTo("1 2 3 Bad 2:3 asdd"));
		assertThat( links.filter("contrary. 1"), equalTo("contrary. 1"));
		assertThat( links.filter("i. 03"), equalTo("i. 03"));
		assertThat( links.filter("ii. 04"), equalTo("ii. 04"));
		assertThat( links.filter("it. 1"), equalTo("it. 1"));
		assertThat( links.filter("Core.2.1"), equalTo("Core.2.1"));
	}
	
	@Test
	public void lineWrapTest() {
		String text = "The same is expressed, Psalm\n\t\t\t\t       73:18. \"Surely thou didst set them in slippery places; thou\n\t\t\t\t       castedst them down into destruction.\"";
		assertThat( links.filter(text), containsString("Ps.73.18"));
	}
	
	public void preventUnusualMatchesTest() {
		
	}
}
