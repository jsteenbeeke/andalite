/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeroensteenbeeke.andalite.java.analyzer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedImport;

public class AnalyzedImportTest {
	private static final String ORG_JUNIT = "org.junit";

	private static final String ORG_JUNIT_TEST = "org.junit.Test";

	private static final String ORG_JUNIT_STAR = "org.junit.*";

	private static final String ORG_JUNIT_ASSERT = "org.junit.Assert";

	private static final String ORG_JUNIT_ASSERT_FALSE = "org.junit.Assert.assertFalse";

	private static final String ORG_JUNIT_ASSERT_EQUALS = "org.junit.Assert.assertEquals";

	private static final String ORG_JUNIT_ASSERT_STAR = "org.junit.Assert.*";

	@Test
	public void testBasicImport() {
		AnalyzedImport i = new AnalyzedImport(loc(), ORG_JUNIT_TEST, false,
				false);

		assertEquals(ORG_JUNIT_TEST, i.getStatement());
		assertFalse(i.isAsterisk());
		assertFalse(i.isStaticImport());
		assertTrue(i.matchesClass(ORG_JUNIT_TEST));
		assertFalse(i.matchesClass(ORG_JUNIT));
		assertFalse(i.matchesClass(ORG_JUNIT_STAR));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_STAR));

		assertFalse(i.importsMethodStatically(ORG_JUNIT_TEST));
		assertFalse(i.importsMethodStatically(ORG_JUNIT));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_STAR));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_EQUALS));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_STAR));

	}

	@Test
	public void testPackageImport() {
		AnalyzedImport i = new AnalyzedImport(loc(), ORG_JUNIT, false, true);

		assertEquals(ORG_JUNIT, i.getStatement());
		assertTrue(i.isAsterisk());
		assertFalse(i.isStaticImport());
		assertTrue(i.matchesClass(ORG_JUNIT_TEST));
		assertFalse(i.matchesClass(ORG_JUNIT));
		assertFalse(i.matchesClass(ORG_JUNIT_STAR));
		assertTrue(i.matchesClass(ORG_JUNIT_ASSERT));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_STAR));

		assertFalse(i.importsMethodStatically(ORG_JUNIT_TEST));
		assertFalse(i.importsMethodStatically(ORG_JUNIT));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_STAR));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_EQUALS));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_STAR));

	}

	@Test
	public void testStaticImport() {
		AnalyzedImport i = new AnalyzedImport(loc(), ORG_JUNIT_ASSERT_FALSE,
				true, false);

		assertEquals(ORG_JUNIT_ASSERT_FALSE, i.getStatement());
		assertFalse(i.isAsterisk());
		assertTrue(i.isStaticImport());
		assertFalse(i.matchesClass(ORG_JUNIT_TEST));
		assertFalse(i.matchesClass(ORG_JUNIT));
		assertFalse(i.matchesClass(ORG_JUNIT_STAR));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_STAR));

		assertFalse(i.importsMethodStatically(ORG_JUNIT_TEST));
		assertFalse(i.importsMethodStatically(ORG_JUNIT));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_STAR));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT));
		assertTrue(i.importsMethodStatically(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_EQUALS));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_STAR));

	}

	@Test
	public void testStaticStarImport() {
		AnalyzedImport i = new AnalyzedImport(loc(), ORG_JUNIT_ASSERT, true,
				true);

		assertEquals(ORG_JUNIT_ASSERT, i.getStatement());
		assertTrue(i.isAsterisk());
		assertTrue(i.isStaticImport());
		assertFalse(i.matchesClass(ORG_JUNIT_TEST));
		assertFalse(i.matchesClass(ORG_JUNIT));
		assertFalse(i.matchesClass(ORG_JUNIT_STAR));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.matchesClass(ORG_JUNIT_ASSERT_STAR));

		assertFalse(i.importsMethodStatically(ORG_JUNIT_TEST));
		assertFalse(i.importsMethodStatically(ORG_JUNIT));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_STAR));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT));
		assertTrue(i.importsMethodStatically(ORG_JUNIT_ASSERT_EQUALS));
		assertTrue(i.importsMethodStatically(ORG_JUNIT_ASSERT_FALSE));
		assertFalse(i.importsMethodStatically(ORG_JUNIT_ASSERT_STAR));

	}

	private Location loc() {
		return new Location(15, 35);
	}
}
