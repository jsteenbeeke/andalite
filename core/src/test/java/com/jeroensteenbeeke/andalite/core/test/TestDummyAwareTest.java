package com.jeroensteenbeeke.andalite.core.test;

import java.io.IOException;

import org.junit.Test;

public class TestDummyAwareTest extends DummyAwareTest {
	@Test
	public void testValidClass() throws IOException {
		getDummy(BaseDummies.BareClass);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidClass() throws IOException {
		getDummy(BaseDummies.OhMyGodThisIsNotARealClass);
	}
}
