package com.jeroensteenbeeke.andalite.java.util;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.lux.TypedResult;
import org.junit.jupiter.api.Test;

import java.util.SortedSet;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterMapTest {
	@Test
	public void testCharacterMap() {
		final String INPUT = "public class FooBar {}";
		final Location ALL = new Location(1, INPUT.length()+1);

		TypedResult<Locations.CharacterMap> result = Locations.mapCharacters(INPUT);

		assertThat(result, isOk());

		Locations.CharacterMap map = result.getObject();

		SortedSet<Location> locations = map.findIn('{', ALL);
		assertThat(locations.size(), equalTo(1));
		final Location start = locations.first();

		locations = map.findIn('}', ALL);
		assertThat(locations.size(), equalTo(1));
		final Location end = locations.first();

		locations = map.findBetweenInclusive('{', start, end);
		assertThat(locations.size(), equalTo(1));

		locations = map.findBetweenInclusive('}', start, end);
		assertThat(locations.size(), equalTo(1));

		locations = map.findBetweenExclusive('{', start, end);
		assertThat(locations.size(), equalTo(1));

		locations = map.findBetweenExclusive('}', start, end);
		assertThat(locations.size(), equalTo(0));
	}
}
