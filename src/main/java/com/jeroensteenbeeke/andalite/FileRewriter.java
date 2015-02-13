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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.io.Files;

public class FileRewriter {
	private static final Logger logger = LoggerFactory
			.getLogger(FileRewriter.class);

	private static final int FIRST_INDEX = 1;

	private final File targetFile;

	private final Multimap<Index, String> mutations;

	private final Multimap<Integer, Index> pointIndexes;

	public FileRewriter(File targetFile) {
		super();
		this.targetFile = targetFile;
		this.mutations = TreeMultimap.create();
		this.pointIndexes = TreeMultimap.create();
	}

	@Nonnull
	public FileRewriter insert(int index, @Nonnull String code) {
		Index location = Index.insertion(index);
		mutations.put(location, code);
		pointIndexes.put(index, location);
		return this;
	}

	@Nonnull
	public FileRewriter replace(int from, int to, @Nonnull String code) {
		Index location = Index.replacement(from, to);
		mutations.put(location, code);
		pointIndexes.put(from, location);
		return this;
	}

	public ActionResult rewrite() {

		try {
			final File temp = File.createTempFile("rewrite", ".java");
			try (final FileInputStream in = new FileInputStream(targetFile);
					final FileOutputStream out = new FileOutputStream(temp)) {
				int point = FIRST_INDEX;
				int data;
				int threshold = FIRST_INDEX - 1;

				while ((data = in.read()) != -1) {

					if (pointIndexes.containsKey(point)) {
						for (Index index : pointIndexes.get(point)) {
							if (mutations.containsKey(index)) {
								for (String insert : mutations.get(index)) {
									out.write(insert.getBytes());

									threshold = Math.max(index.getTo(),
											threshold);
								}
							}
						}
					}

					if (point >= threshold) {
						out.write(data);
					}

					point++;
				}

				out.flush();

				Files.copy(temp, targetFile);
				temp.deleteOnExit();

				return ActionResult.ok();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return ActionResult.error(e.getMessage());
		}
	}

	private static class Index implements Comparable<Index> {
		private final int from;

		private final int to;

		private Index(int from, int to) {
			super();
			this.from = from;
			this.to = to;
		}

		public int getFrom() {
			return from;
		}

		public int getTo() {
			return to;
		}

		@Override
		public int compareTo(Index o) {
			return Integer.compare(from, o.getFrom());
		}

		public static Index insertion(int point) {
			return new Index(point, point);
		}

		public static Index replacement(int from, int to) {
			return new Index(from, to);
		}
	}
}
