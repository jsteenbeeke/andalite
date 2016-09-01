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

/**
 * Test insertion just before a unicode character
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class TestUnicodeInsertion extends DummyAwareTest {
	/**
	 * @see <a href="https://github.com/jsteenbeeke/andalite/issues/3">Andalite
	 *      issue #3</a>
	 */
	@Test
	public void testUnicode() throws IOException {
		// Create a dummy containing a bunch of high-number unicode characters
		File dummy = getDummy(createProvider("🤔🤔🤔🤔🤔🤔🤔🤔"));

		// Make a rewriter for this dummy
		FileRewriter rewriter = new FileRewriter(dummy);

		// Insert the word test in the middle
		rewriter.insert(5, "test");
		// And replace the first few characters
		rewriter.replace(1, 3, "foo");

		// Rewrite it
		ActionResult rewrite = rewriter.rewrite();

		// And make sure it was successful
		assertThat(rewrite, isOk());

		// Now read back the altered file
		try (BufferedReader br = new BufferedReader(new FileReader(dummy))) {
			String line = br.readLine();

			// Find the first occurrence of test
			int first = line.indexOf("test");
			// And whatever comes after
			String rest = line.substring(first + 4);

			// Make sure the word 'test' does not exist in the rest. Andalite
			// issue #3 had
			// rewrites being applied multiple times if they were done in front
			// of a multi-character unicode block
			assertFalse("There are multiple occurrences of test",
					rest.contains("test"));

			// Repeat this test for the part we replaced
			first = line.indexOf("foo");
			rest = line.substring(first + 3);

			assertFalse("There are multiple occurrences of foo",
					rest.contains("foo"));
		}

	}
}
