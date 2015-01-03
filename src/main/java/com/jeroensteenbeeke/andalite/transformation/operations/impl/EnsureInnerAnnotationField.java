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
package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.AnnotationOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.annot.InnerAnnotationCondition;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.annot.MatchAnyInnerAnnotationCondition;

public class EnsureInnerAnnotationField implements AnnotationOperation {
	private final String name;

	private final String type;

	private boolean allowArray = false;

	private InnerAnnotationCondition condition = new MatchAnyInnerAnnotationCondition();

	public EnsureInnerAnnotationField(@Nonnull String name, @Nonnull String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public EnsureInnerAnnotationConditionBuilder satisfying() {
		return new EnsureInnerAnnotationConditionBuilder(this);
	}

	public EnsureInnerAnnotationField whichCouldBeAnArray() {
		this.allowArray = true;
		return this;
	}

	@Override
	public List<Transformation> perform(AnalyzedAnnotation input)
			throws OperationException {
		final String actualName = name != null ? name : "value";

		if (input.hasValueOfType(AnnotationValue.class, name)) {
			AnnotationValue value = input.getValue(AnnotationValue.class, name);

			if (value != null && !condition.isSatisfiedBy(value)) {
				return ImmutableList.of(Transformation.replace(value,
						String.format("%s=@%s()", actualName, type)));
			}
		} else if (allowArray && input.hasValueOfType(ArrayValue.class, name)) {
			ArrayValue value = input.getValue(ArrayValue.class, name);

			BaseValue<?> last = null;

			boolean present = false;

			for (BaseValue<?> baseValue : value.getValue()) {
				if (baseValue instanceof AnnotationValue) {
					AnnotationValue annot = (AnnotationValue) baseValue;
					if (condition.isSatisfiedBy(annot)) {
						present = true;
					}
				}

				last = baseValue;
			}

			if (!present) {
				if (last != null) {
					return ImmutableList.of(Transformation.insertAfter(last,
							String.format(", %s()", type)));
				} else {
					return ImmutableList.of(Transformation.replace(value,
							String.format("%s={@%s()}", actualName, type)));
				}
			}
		} else {
			Location parametersLocation = input.getParametersLocation();

			final String openParen = !input.hasValues()
					&& !input.hasParentheses() ? "(" : "";
			final String closeParen = !input.hasValues()
					&& !input.hasParentheses() ? ")" : "";

			if (parametersLocation == null) {
				return ImmutableList.of(Transformation.insertAfter(input,
						String.format("%s%s=@%s()%s", openParen, actualName,
								type, closeParen)));
			} else {
				String postfix = input.hasValues() ? "," : "";

				return ImmutableList.of(Transformation.insertBefore(
						parametersLocation, String.format("%s%s=@%s()%s%s",
								openParen, actualName, type, postfix,
								closeParen)));
			}
		}

		return ImmutableList.of();
	}

	@Nonnull
	public String getDescription() {
		return String.format("presence of annotation field %s of type @%s",
				name, type);
	}

	@Nonnull
	EnsureInnerAnnotationField setCondition(InnerAnnotationCondition condition) {
		this.condition = condition;
		return this;
	}
}
