package org.andbible.modules.creation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class CorrectCapitalizationTest {

	CorrectCapitalization correctCapitalization = new CorrectCapitalization();
	
	@Test
	public void testFilter() {
		assertThat( correctCapitalization.filter("THE CAT"), equalTo("The Cat"));
		assertThat( correctCapitalization.filter("abc def"), equalTo("abc def"));
	}

}
