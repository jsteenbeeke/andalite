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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.Annotatable;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;

public abstract class AbstractEnsureAnnotation<T extends Annotatable>
		implements IJavaOperation<T> {
	private final String type;

	public AbstractEnsureAnnotation(String type) {
		super();
		this.type = type;
	}

	protected boolean isNewlineBefore() {
		return true;
	}

	protected boolean isNewlineAfter() {
		return false;
	}

	@Override
	public final List<Transformation> perform(T input)
			throws OperationException {
		if (!input.hasAnnotation(type)) {
			final String before = isNewlineBefore() ? "\n\t" : " ";
			final String suffix = isNewlineAfter() ? "\n" : " ";

			final String code = String.format("%s%s@%s%s", before, getPrefix(),
					type, suffix);

			if (!input.getAnnotations().isEmpty()) {
				AnalyzedAnnotation last = input.getAnnotations().get(
						input.getAnnotations().size() - 1);

				return ImmutableList.of(Transformation.insertAfter(last, code));
			}

			return ImmutableList.of(createFirst(input, code));
		}

		return ImmutableList.of();
	}

	protected Transformation createFirst(T input, final String code) {
		return Transformation.insertBefore(input, code);
	}

	protected String getPrefix() {
		return "";
	}

	@Override
	public String getDescription() {
		return "annotation of type @".concat(type);
	}
}