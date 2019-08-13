package com.jeroensteenbeeke.andalite.java.analyzer;

import com.jeroensteenbeeke.andalite.core.test.DummyAwareTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class AnalyzedSourceFileTest extends DummyAwareTest {

	@Test
	public void testEquals() throws IOException {
		File a = getDummy(BaseDummies.BareClass);
		File b = getDummy(BaseDummies.BareClass);

		AnalyzedSourceFile a1 = new ClassAnalyzer(a).analyze().throwIfNotOk(IOException::new);
		AnalyzedSourceFile a2 = new ClassAnalyzer(a).analyze().throwIfNotOk(IOException::new);
		AnalyzedSourceFile b1 = new ClassAnalyzer(b).analyze().throwIfNotOk(IOException::new);

		assertThat(a1, equalTo(a2));
		assertThat(a2, equalTo(a1));

		assertThat(a1, not(equalTo(b1)));
		assertThat(a2, not(equalTo(b1)));
		assertThat(b1, not(equalTo(a1)));
		assertThat(b1, not(equalTo(a2)));
	}
}
