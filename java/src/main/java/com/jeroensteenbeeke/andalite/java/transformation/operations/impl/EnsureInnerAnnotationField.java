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
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IAnnotationOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot.InnerAnnotationCondition;
import com.jeroensteenbeeke.lux.ActionResult;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class EnsureInnerAnnotationField implements IAnnotationOperation {
	private final String name;

	private final String type;

	private boolean allowArray = false;

	private InnerAnnotationCondition condition = annotationValue -> true;

	public EnsureInnerAnnotationField(@NotNull String name, @NotNull String type) {
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
	public List<Transformation> perform(@NotNull AnalyzedAnnotation input)
		throws OperationException {
		final String actualName = name != null ? name : "value";

		if (input.hasValueOfType(AnnotationValue.class, name)) {
			AnnotationValue value = input.getValue(AnnotationValue.class, name);

			if (value != null && !condition.isSatisfiedBy(value)) {
				if (allowArray) {
					return ImmutableList.of(
						value.replace(String.format("{%s, @%s()}", value.toJavaString(), type))

					);
				} else {
					return ImmutableList.of(value.replace(
						String.format("%s=@%s()", actualName, type)));
				}
			}
		} else if (allowArray && input.hasValueOfType(ArrayValue.class, name)) {
			ArrayValue value = input.getValue(ArrayValue.class, name);

			if (value != null) { // Redundant, but IDE keeps complaining
				boolean present = false;

				for (BaseValue<?, ?, ?> baseValue : value.getValue()) {
					if (baseValue instanceof AnnotationValue) {
						AnnotationValue annot = (AnnotationValue) baseValue;
						if (condition.isSatisfiedBy(annot)) {
							present = true;
						}
					}
				}

				if (!present) {
					return ImmutableList.of(value.insertAt(ArrayValue.ArrayValueInsertionPoint.AFTER_LAST_ELEMENT,
														   String.format(", %s()", type)));
				}
			}
		} else {
			final String prefix = input.getValues().isEmpty() ? "" : ", ";

			return ImmutableList.of(input.insertAt(AnalyzedAnnotation.AnnotationInsertionPoint.AFTER_LAST_ARGUMENT,
												   String.format("%s%s=@%s()", prefix, actualName,
																 type)));
		}

		return ImmutableList.of();
	}

	@NotNull
	public String getDescription() {
		return String.format("presence of annotation field %s of type @%s",
							 name, type);
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedAnnotation input) {
		if (input.hasValueNamed(name)) {

			if (input.hasValueOfType(AnnotationValue.class, name)) {
				AnnotationValue value = input.getValue(AnnotationValue.class,
													   name);

				if (value != null && !value.getValue().getType().equals(type)) {
					return ActionResult
						.error("Annotation value %s has incorrect type @%s, expected %s",
							   name, value.getValue().getType(), type);
				}

				return ActionResult.ok();
			} else if (allowArray
				&& input.hasValueOfType(ArrayValue.class, name)) {
				ArrayValue value = input.getValue(ArrayValue.class, name);

				if (value != null) { // Redundant
					for (BaseValue<?, ?, ?> baseValue : value.getValue()) {
						if (baseValue instanceof AnnotationValue) {
							AnnotationValue annot = (AnnotationValue) baseValue;

							if (annot.getValue().getType().equals(type)) {
								return ActionResult.ok();
							}

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

	@NotNull
	EnsureInnerAnnotationField setCondition(InnerAnnotationCondition condition) {
		this.condition = condition;
		return this;
	}
}
