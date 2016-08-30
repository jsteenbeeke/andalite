package com.jeroensteenbeeke.andalite.core.test;

import static com.jeroensteenbeeke.andalite.core.ResultMatchers.isOk;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.FileRewriter;

public class TestUnicodeInsertion extends DummyAwareTest {
	@Test
	public void testUnicode() throws IOException {
		File dummy = getDummy(BaseDummies.Unicode);

		FileRewriter rewriter = new FileRewriter(dummy);
		rewriter.insert(4, "test");

		ActionResult rewrite = rewriter.rewrite();
		assertThat(rewrite, isOk());

		try (BufferedReader br = new BufferedReader(new FileReader(dummy))) {
			String line = br.readLine();

			int first = line.indexOf("test");
			String rest = line.substring(first + 4);

			assertFalse("There are multiple occurrences of test",
					rest.contains("test"));
		}

	}
}
