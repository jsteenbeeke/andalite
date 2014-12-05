package com.jeroensteenbeeke.andalite.analyzer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.CodePoint;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedImport;

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
		CodePoint start = new CodePoint(3, 1);
		CodePoint end = new CodePoint(3, 23);

		return new Location(start, end);
	}
}
