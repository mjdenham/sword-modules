package org.andbible.modules.creation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class OldeEnglishModerniserTest {

	@Test
	public void test() {
		OldeEnglishModerniser moderniser = new OldeEnglishModerniser();
		assertThat( moderniser.filter("start from whence end", true), equalTo("start from where<note n=\"whence\">Auto-modernized</note> end"));
		assertThat( moderniser.filter("start whence end", true), equalTo("start from where<note n=\"whence\">Auto-modernized</note> end"));
		assertThat( moderniser.filter("things. Unto others they", true), equalTo("things. To<note n=\"unto\">Auto-modernized</note> others they")); 
	}

}
