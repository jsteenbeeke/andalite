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

import com.jeroensteenbeeke.lux.ActionResult;

import java.io.File;

import javax.annotation.Nonnull;

public class Transformation {
	private final int from;

	private final int to;

	private final String code;

	private final boolean invalidatesNavigation;

	private Transformation(int point, String code) {
		this(point, point, code, false);
	}

	private Transformation(int from, int to, String code, boolean invalidatesNavigation) {
		this.from = from;
		this.to = to;
		this.code = code;
		this.invalidatesNavigation = invalidatesNavigation;
	}

	public Transformation whichInvalidatesNavigation() {
		return new Transformation(from, to, code, true);
	}

	public ActionResult applyTo(@Nonnull File targetFile) {
		FileRewriter rewriter = new FileRewriter(targetFile);
		rewriter.replace(from, to, code);
		return rewriter.rewrite();
	}

	public boolean invalidatesNavigation() {
		return invalidatesNavigation;
	}

	public static Transformation replace(@Nonnull IReplaceable replaceable, @Nonnull String code) {
		Location location = replaceable.getLocation();

		return new Transformation(location.getStart(), location.getEnd()+1, code, false);
	}

	public static <T extends IInsertionPointProvider<T, I>, I extends Enum<I> & IInsertionPoint<? super T>> Transformation atInsertionPoint(@Nonnull T container,
																																	@Nonnull IInsertionPoint<? super T> point,
																																	@Nonnull String code) {
		return new Transformation(point.position(container), code);
	}

}
