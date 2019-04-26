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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.Annotatable;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.lux.ActionResult;

import java.util.List;

public abstract class AbstractEnsureAnnotation<T extends Annotatable<T, I>, I extends Enum<I> & IInsertionPoint<T>>
	implements IJavaOperation<T> {
	private final String type;

	AbstractEnsureAnnotation(String type) {
		this.type = type;
	}

	protected boolean isNewlineBefore(T input) {
		return true;
	}

	protected boolean isNewlineAfter(T input) {
		return false;
	}

	@Override
	public final List<Transformation> perform(T input) {
		if (!input.hasAnnotation(type)) {
			final String before = isNewlineBefore(input) ? "\n\t" : " ";
			final String suffix = isNewlineAfter(input) ? "\n" : " ";

			final String code = String.format("%s%s@%s%s", before, getPrefix(),
											  type, suffix);

			return ImmutableList.of(input.insertAt(input.getAnnotationInsertPoint(), code));
		}

		return ImmutableList.of();
	}

	protected String getPrefix() {
		return "";
	}

	@Override
	public String getDescription() {
		return "annotation of type @".concat(type);
	}

	@Override
	public ActionResult verify(T input) {
		boolean hasAnnotation = input.hasAnnotation(type);
		return hasAnnotation ? ActionResult.ok()
			: ActionResult.error("Annotation @%s not present", type);
	}


}
