package com.jeroensteenbeeke.andalite.core.test;

import java.io.IOException;

import org.junit.Test;

/**
 * Test if DummyAwareTest works properly
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class TestDummyAwareTest extends DummyAwareTest {
	/**
	 * Test loading of a valid class
	 */
	@Test
	public void testValidClass() throws IOException {
		getDummy(BaseDummies.BareClass);
	}

	/**
	 * Test loading of a invalid class
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidClass() throws IOException {
		getDummy(BaseDummies.OhMyGodThisIsNotARealClass);
	}
}
