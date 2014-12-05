package com.jeroensteenbeeke.andalite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.io.Files;

public class FileRewriter {
	private static final int FIRST_LINE = 1;

	private static final int FIRST_COLUMN = 1;

	private final File targetFile;

	private final Multimap<CodePoint, String> insertions;

	public FileRewriter(File targetFile) {
		super();
		this.targetFile = targetFile;
		this.insertions = TreeMultimap.create();
	}

	@Nonnull
	public FileRewriter insert(int line, int column, @Nonnull String code) {
		return insert(new CodePoint(line, column), code);
	}

	@Nonnull
	public FileRewriter insert(@Nonnull CodePoint codePoint,
			@Nonnull String code) {
		insertions.put(codePoint, code);
		return this;
	}

	public ActionResult rewrite() {

		try {
			final File temp = File.createTempFile("rewrite", ".java");
			try (final FileInputStream in = new FileInputStream(targetFile);
					final FileOutputStream out = new FileOutputStream(temp)) {

				int line = FIRST_LINE;
				int data = -1;
				int column = FIRST_COLUMN;

				while ((data = in.read()) != -1) {

					final CodePoint here = new CodePoint(line, column);

					if (insertions.containsKey(here)) {
						for (String insert : insertions.get(here)) {
							out.write(insert.getBytes());
						}
					}

					out.write(data);

					column++;

					if (data == '\n') {
						line++;
						column = FIRST_COLUMN;
					}
				}

				out.flush();

				Files.copy(temp, targetFile);

				return ActionResult.ok();
			}
		} catch (IOException e) {
			return ActionResult.error(e.getMessage());
		}
	}
}
