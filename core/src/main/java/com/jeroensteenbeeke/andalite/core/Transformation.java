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

public class Transformation {
	private final int from;

	private final int to;

	private final String code;

	private Transformation(int point, String code) {
		this(point, point, code);
	}

	private Transformation(int from, int to, String code) {
		super();
		this.from = from;
		this.to = to;
		this.code = code;
	}

	public ActionResult applyTo(@Nonnull File targetFile) {
		FileRewriter rewriter = new FileRewriter(targetFile);
		rewriter.replace(from, to, code);
		return rewriter.rewrite();
	}

	public static Transformation insertBefore(@Nonnull ILocatable locatable,
			@Nonnull String code) {
		return insertBefore(locatable.getLocation(), code);
	}

	public static Transformation insertBefore(@Nonnull Location location,
			@Nonnull String code) {
		return new Transformation(location.getStart(), code);
	}

	public static Transformation insertAt(int index, @Nonnull String code) {
		return new Transformation(index, code);
	}

	public static Transformation insertAfter(@Nonnull ILocatable locatable,
			@Nonnull String code) {
		return insertAfter(locatable.getLocation(), code);
	}

	public static Transformation insertAfter(@Nonnull Location location,
			@Nonnull String code) {
		return new Transformation(location.getEnd() + 1, code);
	}

	public static Transformation replace(@Nonnull ILocatable locatable,
			@Nonnull String code) {
		return replace(locatable.getLocation(), code);
	}

	public static Transformation replace(@Nonnull Location location,
			@Nonnull String code) {
		return new Transformation(location.getStart(), location.getEnd() + 1,
				code);
	}

}