package org.andbible.modules.creation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class RomanTest {

	@Test
	public void testToLong() {
		assertThat( Roman.toLong("vii"), equalTo(7L));
		assertThat( Roman.toLong("xxcvii"), equalTo(87L));

	}

	@Test
	public void testToRoman() {
		assertThat( Roman.toRoman(7), equalTo("VII"));
	}

}
