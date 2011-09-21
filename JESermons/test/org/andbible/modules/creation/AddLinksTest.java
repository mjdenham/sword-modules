package org.andbible.modules.creation;


import java.util.regex.Pattern;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AddLinksTest {

	AddLinks links = new AddLinks();

	@Test
	public void simpleNoLinkTest() {
		assertThat( links.filter("abc def"), equalTo("abc def"));
	}

	@Test
	public void simpleLinkTest() {
		assertThat( links.filter("Deuteronomy 32:35"), equalTo("<reference osisRef=\"Deut.32.35\">Deuteronomy 32:35</reference>"));
	}
	
	@Test
	public void confusingTextTest() {
		assertThat( links.filter("1 2 3 Bad 2:3 asdd"), equalTo("1 2 3 Bad 2:3 asdd"));
	}

	@Test
	public void lineWrapTest() {
		String text = "The same is expressed, Psalm\n\t\t\t\t       73:18. \"Surely thou didst set them in slippery places; thou\n\t\t\t\t       castedst them down into destruction.\"";
		assertThat( links.filter(text), containsString("Ps.73.18"));
	}
}
