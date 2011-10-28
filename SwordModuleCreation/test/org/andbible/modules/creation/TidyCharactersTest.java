package org.andbible.modules.creation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class TidyCharactersTest {

	private TidyCharacters tidyCharacters = new TidyCharacters();
	
	@Test
	public void testFilter() {
		assertThat( tidyCharacters.filter("okay -- this is it"), equalTo("okay - this is it"));
		assertThat( tidyCharacters.filter("okay <!-- this is a comment"), equalTo("okay <!-- this is a comment"));
		assertThat( tidyCharacters.filter("say?-- Father"), equalTo("say?- Father"));
	}

}
