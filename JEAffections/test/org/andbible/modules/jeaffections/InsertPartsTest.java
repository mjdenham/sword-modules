package org.andbible.modules.jeaffections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class InsertPartsTest {

	String testPart1 = 
"		     __________________________________________________________________\n"+
"\n"+
"		     RELIGIOUS AFFECTIONS.\n"+
"\n"+
"		     PART 1.\n"+
"\n"+
"		     CONCERNING THE NATURE OF THE AFFECTIONS AND THEIR IMPORTANCE IN\n"+
"		     RELIGION.\n"+
"\n"+
"		     1 Peter 1:8: Whom having not seen, ye love; in whom, though now ye see\n"+
"		     him not, yet believing, ye rejoice with joy unspeakable and full of\n"+
"		     glory.\n"+
"\n"+
"		     In these words, the apostle represents the state of the minds of the\n";
			
	
	@Test
	public void test1() {
		String result = new InsertParts().filter(testPart1);
		assertThat( result, containsString("</p>\n</div>\n<div type=\"chapter\" osisID=\"Part 1\">"));
		assertThat( result, containsString("<title>CONCERNING"));
	}

	String testPart2 = 
"		     __________________________________________________________________\n"+
"\n"+
"		     PART II.\n"+
"\n"+
"		     SHOWING WHAT ARE NO CERTAIN SIGNS THAT RELIGIOUS AFFECTIONS ARE\n"+
"		     GRACIOUS, OR THAT THEY ARE NOT.\n"+
"\n"+
"		     If anyone, on the reading of what has been just now said, is ready to\n"+
"		     acquit himself, and say, \"I am not one of those who have no religious\n"+
"		     affections; I am often greatly moved with the consideration of the\n";

	@Test
	public void test2() {
		String result = new InsertParts().filter(testPart2);
		assertThat( result, containsString("</p>\n</div>\n<div type=\"chapter\" osisID=\"Part II\">"));
		assertThat( result, containsString("<title>SHOWING"));
	}

}
