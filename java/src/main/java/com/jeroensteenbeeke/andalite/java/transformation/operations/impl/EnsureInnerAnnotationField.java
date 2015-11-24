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

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IAnnotationOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.InnerAnnotationCondition;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.MatchAnyInnerAnnotationCondition;

public class EnsureInnerAnnotationField implements IAnnotationOperation {
	private final String name;

	private final String type;

	private boolean allowArray = false;

	private InnerAnnotationCondition condition = new MatchAnyInnerAnnotationCondition();

	public EnsureInnerAnnotationField(@Nonnull String name, @Nonnull String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public EnsureInnerAnnotationConditionBuilder ifNotAlreadyPresentWith() {
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
				if (allowArray) {
					return ImmutableList.of(Transformation.insertAt(value
							.getLocation().getStart() + 1, "{"), Transformation
							.insertAt(value.getLocation().getEnd() + 2,
									String.format(", @%s()}", type)));
				} else {
					return ImmutableList.of(Transformation.replace(value,
							String.format("%s=@%s()", actualName, type)));
				}
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

	@Override
	public ActionResult verify(AnalyzedAnnotation input) {
		if (input.hasValueNamed(name)) {

			if (input.hasValueOfType(AnnotationValue.class, name)) {
				AnnotationValue value = input.getValue(AnnotationValue.class,
						name);

				if (!value.getValue().getType().equals(type)) {
					return ActionResult
							.error("Annotation value %s has incorrect type @%s, expected %s",
									name, value.getValue().getType(), type);
				}
				if (!condition.isSatisfiedBy(value)) {
					return ActionResult
							.error("Annotation value %s does not satisfy condition %s",
									name, condition.toString());
				}

				return ActionResult.ok();
			} else if (allowArray
					&& input.hasValueOfType(ArrayValue.class, name)) {
				ArrayValue value = input.getValue(ArrayValue.class, name);

				for (BaseValue<?> baseValue : value.getValue()) {
					if (baseValue instanceof AnnotationValue) {
						AnnotationValue annot = (AnnotationValue) baseValue;
						if (condition.isSatisfiedBy(annot)) {
							return ActionResult.ok();
						}
					}
				}
				return ActionResult.error(
						"No array value in %s that satisfies %s", name,
						condition.toString());
			}
		}

		return ActionResult.error("No value named %s", name);
	}

	@Nonnull
	EnsureInnerAnnotationField setCondition(InnerAnnotationCondition condition) {
		this.condition = condition;
		return this;
	}
}
