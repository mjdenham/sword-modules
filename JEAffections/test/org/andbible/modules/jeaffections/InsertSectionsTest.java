package org.andbible.modules.jeaffections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class InsertSectionsTest {
	
	String testSection1 = 
"     __________________________________________________________________\n"+
"\n"+
"     I. It is no sign one way or the other, that religious affections are";
	
	@Test
	public void testFilter() {
		String result = new InsertSections().filter(testSection1);
		assertThat( result, containsString("</p>\n</div>\n<div type=\"chapter\" osisID=\"Section 1.1\">"));
	}

	@Test
	public void testFalseText() {
		String in = 
"    judgment of the reader of the following treatise.\n"+
"\n"+
"   I am sensible it is much more difficult to judge impartially of that";

		String result = new InsertSections().filter(in);
		assertThat( result, equalTo(in));
	}
}
