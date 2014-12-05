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
