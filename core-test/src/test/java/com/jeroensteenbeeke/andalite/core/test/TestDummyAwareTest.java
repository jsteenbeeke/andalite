package com.jeroensteenbeeke.andalite.core.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestDummyAwareTest extends DummyAwareTest {
	@Test
	public void testValidClass() throws IOException {
		getDummy(BaseDummies.BareClass);
	}

	@Test
	public void testInvalidClass() {
		assertThrows(IllegalArgumentException.class, () -> getDummy(BaseDummies.OhMyGodThisIsNotARealClass));
	}
}
