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
	private static final int FIRST_INDEX = 1;

	private final File targetFile;

	private final Multimap<Integer, String> insertions;

	public FileRewriter(File targetFile) {
		super();
		this.targetFile = targetFile;
		this.insertions = TreeMultimap.create();
	}

	@Nonnull
	public FileRewriter insert(@Nonnull int index, @Nonnull String code) {
		insertions.put(index, code);
		return this;
	}

	public ActionResult rewrite() {

		try {
			final File temp = File.createTempFile("rewrite", ".java");
			try (final FileInputStream in = new FileInputStream(targetFile);
					final FileOutputStream out = new FileOutputStream(temp)) {
				int index = FIRST_INDEX;
				int data;

				while ((data = in.read()) != -1) {

					if (insertions.containsKey(index)) {
						for (String insert : insertions.get(index)) {
							out.write(insert.getBytes());
						}
					}

					out.write(data);

					index++;
				}

				out.flush();

				Files.copy(temp, targetFile);
				temp.deleteOnExit();

				return ActionResult.ok();
			}
		} catch (IOException e) {
			return ActionResult.error(e.getMessage());
		}
	}
}
