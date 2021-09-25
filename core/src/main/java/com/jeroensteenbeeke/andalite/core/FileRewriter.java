/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeroensteenbeeke.andalite.core;

import com.google.common.collect.Lists;
import com.google.common.collect.TreeMultimap;
import com.google.common.io.Files;
import com.jeroensteenbeeke.lux.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class FileRewriter {
	private static final Logger logger = LoggerFactory
		.getLogger(FileRewriter.class);

	private static final int FIRST_INDEX = 1;

	private final File targetFile;

	private final TreeMultimap<Index, String> mutations;

	private final TreeMultimap<Integer, Index> pointIndexes;

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
			logger.debug("Input: {}", targetFile.getAbsolutePath());
			final File temp = File.createTempFile("rewrite", ".java");
			try (final FileInputStream in = new FileInputStream(targetFile);
				 final FileOutputStream out = new FileOutputStream(temp)) {
				int point = FIRST_INDEX;
				int data;
				int threshold = FIRST_INDEX - 1;
				int unicodeBytesRemaining = 0;

				List<Integer> previous = Lists.newLinkedList();

				StringBuilder output = new StringBuilder();
				boolean inUnicode = false;

				while ((data = in.read()) != -1) {
					if (unicodeBytesRemaining == 0 && !inUnicode) {
						unicodeBytesRemaining = getUnicodeBytesRemaining(data, previous);
						inUnicode = unicodeBytesRemaining > 0;
					}

					if (pointIndexes.containsKey(point)) {
						for (Index index : pointIndexes.get(point)) {
							if (mutations.containsKey(index)) {
								for (String insert : mutations.get(index)) {
									out.write(insert.getBytes());

									output.append("\u001B[33m");
									output.append(insert);
									output.append("\u001B[0m");

									threshold = Math.max(index.getTo(),
														 threshold);
								}
							}
						}
					}

					if (point >= threshold) {
						out.write(data);
						output.append((char) data);
						previous.add(data);
						if (previous.size() > 20) {
							previous.remove(0);
						}
					}

					if (unicodeBytesRemaining == 0) {
						point++;
						inUnicode = false;
					} else {
						unicodeBytesRemaining--;

					}

				}

				out.flush();

				logger.debug("Outputted: {}", output);

				SortedSet<Integer> remainingIndexes = pointIndexes.keySet().tailSet(point);
				if (!remainingIndexes.isEmpty()) {
					if (remainingIndexes.size() > 1 || !remainingIndexes.contains(point)) {

						return ActionResult.error("Transformations beyond file end! Requested: %s, but index is %d",
												  remainingIndexes
													  .stream()
													  .map(i -> Integer.toString(i))
													  .collect(Collectors.joining(", ", "{", "}"))
							, point);
					} else {
						// Transformations remaining for end of file
						for (Index index : pointIndexes.get(point)) {
							if (mutations.containsKey(index)) {
								for (String insert : mutations.get(index)) {
									out.write(insert.getBytes());

									output.append("\u001B[33m");
									output.append(insert);
									output.append("\u001B[0m");

									threshold = Math.max(index.getTo(),
														 threshold);
								}
							}
						}
					}
				}

				Files.copy(temp, targetFile);
				temp.deleteOnExit();

				return ActionResult.ok();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return ActionResult.error(e.getMessage());
		}
	}

	private int getUnicodeBytesRemaining(int data, List<Integer> previous) {
		int byte8 = binaryfy(data, 0x80);
		int byte7 = binaryfy(data, 0x40);
		int byte6 = binaryfy(data, 0x20);
		int byte5 = binaryfy(data, 0x10);
		int byte4 = binaryfy(data, 0x08);


		if (byte8 == 0) {
			return 0;
		} else if (byte8 == 1 && byte7 == 1 && byte6 == 0) {
			return 1;
		} else if (byte8 == 1 && byte7 == 1 && byte6 == 1 && byte5 == 0) {
			return 2;
		} else if (byte8 == 1 && byte7 == 1 && byte6 == 1 && byte5 == 1
			&& byte4 == 0) {
			return 3;
		}

		throw new IllegalArgumentException(
			String.format(
				"I have no idea what to do with this character: %s [%d%d%d%d%d] (previous: %s)",
				(char) data, byte8, byte7, byte6, byte5, byte4, previous
					.stream()
					.map(Integer::toBinaryString)
					.collect(Collectors.joining(", "))));

	}

	private int binaryfy(int data, int mask) {
		return (data & mask) == mask ? 1 : 0;
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
