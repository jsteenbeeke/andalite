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

package com.jeroensteenbeeke.andalite.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.io.Files;

/**
 * I/O class for transforming files.
 *
 * @author Jeroen Steenbeeke
 */
public class FileRewriter {
	private static final Logger logger = LoggerFactory
			.getLogger(FileRewriter.class);

	private static final int FIRST_INDEX = 1;

	// The file that should be rewritten
	private final File targetFile;

	// A map that stores all fragments (value) based on where they should be
	// placed (key)
	private final Multimap<IndexRange, String> mutations;

	// A map that stores all IndexRange objects (values; from-to pairs) based on
	// their starting (key; the from part of the index) location
	private final Multimap<Integer, IndexRange> pointIndexes;

	/**
	 * Creates a new rewriter for the indicated file
	 * 
	 * @param targetFile
	 *            The file to rewrite
	 */
	public FileRewriter(File targetFile) {
		super();
		this.targetFile = targetFile;
		this.mutations = TreeMultimap.create();
		this.pointIndexes = TreeMultimap.create();
	}

	/**
	 * Sets a given fragment to be inserted at the specified index
	 * 
	 * @param index
	 *            The 1-based index (character) where the fragment should be
	 *            placed in the file
	 * @param fragment
	 *            The fragment to place in the file
	 * @return The current {@code FileRewriter}
	 */
	@Nonnull
	public FileRewriter insert(int index, @Nonnull String fragment) {
		IndexRange location = IndexRange.insertion(index);
		mutations.put(location, fragment);
		pointIndexes.put(index, location);
		return this;
	}

	/**
	 * Sets a given fragment to replace a specified range of characters in the
	 * file
	 * 
	 * @param from
	 *            The starting (1-based) index of the location to be replaced
	 * @param to
	 *            The ending (0-based) index of the location to be replaced
	 * @param fragment
	 *            The fragment to replace the given range
	 * @return The current {@code FileRewriter}
	 */
	@Nonnull
	public FileRewriter replace(int from, int to, @Nonnull String fragment) {
		IndexRange location = IndexRange.replacement(from, to);
		mutations.put(location, fragment);
		pointIndexes.put(from, location);
		return this;
	}

	/**
	 * Perform all set actions on the file that was passed to the constructor
	 * 
	 * @return An {@code ActionResult} indicated success, or the reason for
	 *         failure
	 */
	public ActionResult rewrite() {
		try {
			logger.debug("Input: {}", targetFile.getAbsolutePath());
			// Make changes to a temp file first
			final File temp = File.createTempFile("rewrite", ".java");

			// Create read and write streams
			try (final FileInputStream in = new FileInputStream(targetFile);
					final FileOutputStream out = new FileOutputStream(temp)) {
				int point = FIRST_INDEX; // The current index
				int data;
				int threshold = FIRST_INDEX - 1; // Helper variable to keep
													// track of replace
													// operations
				int lastInsert = FIRST_INDEX - 1; // Helper variable to keep
													// track of last position
													// something was added

				StringBuilder debugOutput = new StringBuilder();

				// Variables for unicode handling
				boolean inUnicode = false;
				int unicodeBytesRemaining = 0;
				List<Integer> previous = Lists.newLinkedList();

				while ((data = in.read()) != -1) {
					// If we are not already reading the additional bytes of a
					// unicode character, see if the next
					// character marks the start of one
					if (unicodeBytesRemaining == 0 && !inUnicode) {
						unicodeBytesRemaining = getUnicodeBytesRemaining(data,
								previous);
						inUnicode = unicodeBytesRemaining > 0; // If there is at
																// least 1 more
																// byte of
																// unicode, set
																// the flag to
																// true
					}

					// Check if we did not already insert something at this
					// point (can happen when inserting something just before a
					// unicode character)
					if (point > lastInsert) {
						// Check if the current point in the file has any ranges
						// registered
						if (pointIndexes.containsKey(point)) {
							// If so, get all ranges for the current point
							for (IndexRange index : pointIndexes.get(point)) {
								// Then check if there are any changes scheduled
								// at
								// this range
								if (mutations.containsKey(index)) {
									// If so, get all changes
									for (String insert : mutations.get(index)) {
										// And write this to the output stream
										out.write(insert.getBytes());
										lastInsert = point;

										// Include it in the debug output
										debugOutput.append("\u001B[33m");
										debugOutput.append(insert);
										debugOutput.append("\u001B[0m");

										threshold = Math.max(index.getTo(),
												threshold);
									}
								}
							}
						}
					}

					// The only times when point is lower than threshold is when
					// a fragment replaces an existing fragment. In these
					// cases we want to skip writing data
					if (point >= threshold) {
						out.write(data);
						debugOutput.append((char) data);
						previous.add(data);
						if (previous.size() > 20) {
							previous.remove(0);
						}
					}

					// Only increment the point variable if we are not currently
					// parsing a unicode character
					if (unicodeBytesRemaining == 0) {
						point++;
						inUnicode = false;
					} else {
						unicodeBytesRemaining--;

					}

				}

				out.flush();

				logger.debug("Outputted: {}", debugOutput);

				Files.copy(temp, targetFile);
				temp.deleteOnExit();

				return ActionResult.ok();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return ActionResult.error(e.getMessage());
		}
	}

	/**
	 * Checks a read byte of data, to see if it is the first byte in a unicode
	 * character sequence
	 * 
	 * Unicode characters span multiple bytes, yet only count for a single
	 * character where indexes are concerned
	 * 
	 * @param data
	 *            The data read from an {@code InputStream}, to check for
	 *            unicode markers
	 * @param previous
	 *            The previous bytes of data read from an input stream, used for
	 *            debugging purposes
	 * @return The number of bytes to expect after this one, that comprise the
	 *         same character
	 */
	private int getUnicodeBytesRemaining(int data, List<Integer> previous) {
		int bit8 = binaryfy(data, 0x80);
		int bit7 = binaryfy(data, 0x40);
		int bit6 = binaryfy(data, 0x20);
		int bit5 = binaryfy(data, 0x10);
		int bit4 = binaryfy(data, 0x08);

		if (bit8 == 0) {
			// If the most significant bit is a 0, this is a character
			// that only takes a single byte, so no further character need to be
			// read
			return 0;
		} else if (bit8 == 1 && bit7 == 1 && bit6 == 0) {
			// Byte has mask 110xxxxx, meaning this is a 2-byte character
			return 1;
		} else if (bit8 == 1 && bit7 == 1 && bit6 == 1 && bit5 == 0) {
			// Byte has mask 1110xxxx, meaning this is a 3-byte character
			return 2;
		} else if (bit8 == 1 && bit7 == 1 && bit6 == 1 && bit5 == 1
				&& bit4 == 0) {
			// Byte has mask 11110xxx, meaning this is a 4-byte character
			return 3;
		}

		// Byte has an unrecognized character, and is therefore not according to
		// the unicode spec. Usually
		// this means that an earlier result of this method was not handled
		// properly, and that a non-leading
		// byte is being passed to this method
		throw new IllegalArgumentException(String.format(
				"I have no idea what to do with this character: %s [%d%d%d%d%d] (previous: %s)",
				(char) data, bit8, bit7, bit6, bit5, bit4,
				previous.stream().map(Integer::toBinaryString)
						.collect(Collectors.joining(", "))));

	}

	/**
	 * Checks if a certain bit-mask is fully present in the given data
	 * 
	 * @param data
	 *            The data to check
	 * @param mask
	 *            The bit-mask to check against
	 * @return {@code 1} if all bits in the mask are present, {@code 0}
	 *         otherwise
	 */
	private int binaryfy(int data, int mask) {
		return (data & mask) == mask ? 1 : 0;
	}

	/**
	 * A range of two locations that indicate the location of a code change
	 *
	 * @author Jeroen Steenbeeke
	 */
	private static class IndexRange implements Comparable<IndexRange> {
		private final int from;

		private final int to;

		/**
		 * Creates a new IndexRange
		 * 
		 * @param from
		 *            The start of the range. Indexes are 1-based, so should be
		 *            1 or more
		 * @param to
		 *            The end of the range. Index are 1-based, so should be 1 or
		 *            more
		 */
		private IndexRange(int from, int to) {
			super();
			this.from = from;
			this.to = to;
		}

		/**
		 * Get the lower-bound index of this range
		 * @return A 1-based index
		 */
		public int getFrom() {
			return from;
		}

		/**
		 * Gets the upper-bound index of this range
		 * @return A 1-based index
		 */
		public int getTo() {
			return to;
		}

		@Override
		public int compareTo(IndexRange o) {
			return Integer.compare(from, o.getFrom());
		}

		/**
		 * Create a new insertion range at the given index
		 * @param point A 1-based index
		 * @return An {@code IndexRange} that does not span any characters
		 */
		public static IndexRange insertion(int point) {
			return new IndexRange(point, point);
		}

		/**
		 * Creates a new insertion range for the given lower and upper bound
		 * @param from The lower-bound 1-based index
		 * @param to The upper-bound 1-based index
		 * @return An {@code IndexRange} that spans the given interval
		 */
		public static IndexRange replacement(int from, int to) {
			return new IndexRange(from, to);
		}
	}
}
