package org.andbible.modules.creation;

import org.junit.Test;


public class InsertParagraphsTest {

	private InsertParagraphs insertParagraphs = new InsertParagraphs();
	@Test
	public void simpleTest() {
		System.out.println(insertParagraphs.filter("\n     \n"));
	}
}
