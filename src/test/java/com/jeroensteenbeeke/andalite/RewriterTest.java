package com.jeroensteenbeeke.andalite;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.jeroensteenbeeke.andalite.ActionResult;
import com.jeroensteenbeeke.andalite.FileRewriter;

@Ignore
public class RewriterTest {
	@Test
	public void testRewriter() {
		FileRewriter rewriter = new FileRewriter(new File(
				"src/test/java/test.txt"));
		rewriter.insert(2, 5, " ");
		ActionResult result = rewriter.rewrite();

		assertEquals(null, result.getMessage());
		assertTrue(result.isOk());
	}
}
