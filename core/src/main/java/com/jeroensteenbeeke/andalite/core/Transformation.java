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

import javax.annotation.Nonnull;

/**
 * Representation of a file transformation
 * 
 * @author Jeroen Steenbeeke
 */
public class Transformation {
	private final int from;

	private final int to;

	private final String code;

	/**
	 * Creates an in-place transformation (insertion)
	 * 
	 * @param point
	 *            The 1-based index to place the code
	 * @param code
	 *            The code to place
	 */
	private Transformation(int point, String code) {
		this(point, point, code);
	}

	/**
	 * Creates a transformation replacing a range of a file with the indicated
	 * code
	 * 
	 * @param from
	 *            The 1-based starting index of the code
	 * @param to
	 *            The 1-based end index of the code
	 * @param code
	 *            The code to insert
	 */
	private Transformation(int from, int to, String code) {
		super();
		this.from = from;
		this.to = to;
		this.code = code;
	}

	/**
	 * Applies this transformation to the file using a {@code FileRewriter}
	 * 
	 * @param targetFile
	 *            The file to transform
	 * @return An {@code ActionResult} instance indicating whether or not the
	 *         transformation was successful
	 */
	@Nonnull
	public ActionResult applyTo(@Nonnull File targetFile) {
		FileRewriter rewriter = new FileRewriter(targetFile);
		rewriter.replace(from, to, code);
		return rewriter.rewrite();
	}

	/**
	 * Creates a transformation that inserts code before the given locatable
	 * 
	 * @param locatable
	 *            The source construct to use as a reference
	 * @param code
	 *            The code to insert before this reference
	 * @return A transformation that inserts the code before the given locatable
	 */
	@Nonnull
	public static Transformation insertBefore(@Nonnull ILocatable locatable, @Nonnull String code) {
		return insertBefore(locatable.getLocation(), code);
	}

	/**
	 * Creates a transformation that inserts code before the given location
	 * 
	 * @param location
	 *            The location the code should be placed in front of
	 * @param code
	 *            The code to insert before this reference
	 * @return A transformation that inserts the code before the given location
	 */
	@Nonnull
	public static Transformation insertBefore(@Nonnull Location location, @Nonnull String code) {
		return new Transformation(location.getStart(), code);
	}

	/**
	 * Creates a transformation that inserts code at a given index
	 * 
	 * @param index
	 *            The exact (1-based) index the code should be placed
	 * @param code
	 *            The code to insert before this reference
	 * @return A transformation that inserts the code at the given index
	 */
	@Nonnull
	public static Transformation insertAt(int index, @Nonnull String code) {
		return new Transformation(index, code);
	}

	/**
	 * Creates a transformation that inserts code after a given
	 * {@code ILocatable}
	 * 
	 * @param locatable
	 *            The construct to place the code after
	 * @param code
	 *            The code to place
	 * @return A transformation that inserts the code after the given construct
	 */
	@Nonnull
	public static Transformation insertAfter(@Nonnull ILocatable locatable, @Nonnull String code) {
		return insertAfter(locatable.getLocation(), code);
	}

	/**
	 * Creates a transformation that inserts code after a given {@code Location}
	 * 
	 * @param location
	 *            The location to place the code after
	 * @param code
	 *            The code to place
	 * @return A transformation that inserts the code after the given location
	 */

	@Nonnull
	public static Transformation insertAfter(@Nonnull Location location, @Nonnull String code) {
		return new Transformation(location.getEnd() + 1, code);
	}

	/**
	 * Replaces the given construct with the given code
	 * 
	 * @param locatable
	 *            The source construct to replace
	 * @param code
	 *            The code to replace it with
	 * @return A transformation that will replace the given construct
	 */
	@Nonnull
	public static Transformation replace(@Nonnull ILocatable locatable, @Nonnull String code) {
		return replace(locatable.getLocation(), code);
	}

	/**
	 * Replaces whatever is at the given location, with the given code
	 * 
	 * @param location
	 *            The location where we want to place the code
	 * @param code
	 *            The code to place
	 * @return A transformation that will replace the given location with the
	 *         given code
	 */
	@Nonnull
	public static Transformation replace(@Nonnull Location location, @Nonnull String code) {
		return new Transformation(location.getStart(), location.getEnd() + 1, code);
	}

}