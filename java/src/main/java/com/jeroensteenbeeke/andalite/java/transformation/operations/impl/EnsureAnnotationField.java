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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IAnnotationOperation;

public abstract class EnsureAnnotationField<T> implements IAnnotationOperation {
	private final String name;

	private final T expectedValue;

	private final Class<? extends BaseValue<T,?,?>> expectedType;

	public EnsureAnnotationField(@NotNull String name,
								 @NotNull Class<? extends BaseValue<T,?,?>> expectedType,
								 @NotNull T value) {
		super();
		this.name = name;
		this.expectedType = expectedType;
		this.expectedValue = value;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedAnnotation input)
		throws OperationException {
		final String actualName = name != null ? name : "value";

		if (input.hasValueOfType(expectedType, name)) {
			BaseValue<T,?,?> value = input.getValue(expectedType, name);

			if (value != null
				&& !Objects.equals(value.getValue(), expectedValue)) {
				return ImmutableList.of(value.replace(String
														  .format("%s=%s", actualName, format(expectedValue))));
			}
		} else if (input.hasValueOfType(ArrayValue.class, name)) {
			ArrayValue value = input.getValue(ArrayValue.class, name);

			boolean present = false;

			for (BaseValue<?,?,?> baseValue : value.getValue()) {
				if (Objects.equals(baseValue.getValue(), expectedValue)) {
					present = true;
				}
			}

			if (!present) {

				return ImmutableList.of(value.insertAt(ArrayValue.ArrayValueInsertionPoint.AFTER_LAST_ELEMENT, format(expectedValue)));
			}
		} else {
			return ImmutableList.of(input.insertAt(AnalyzedAnnotation.AnnotationInsertionPoint.AFTER_LAST_ARGUMENT, String.format("%s%s=%s",
																																  input.hasValues() ? ", " : "",
																																  actualName, format(expectedValue)
																																  )));
		}

		return ImmutableList.of();
	}

	public abstract String format(T value);

	@Override
	public String getDescription() {
		return String.format("presence of annotation field %s with value %s",
							 name, format(expectedValue));
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedAnnotation input) {
		if (input.hasValueOfType(expectedType, name)) {
			BaseValue<T,?,?> value = input.getValue(expectedType, name);

			String expected = format(expectedValue);

			if (value != null) { // Redundant
				String observed = format(value.getValue());

				if (observed.equals(expected)) {
					return ActionResult.ok();
				}
			}

			return ActionResult.error(
				"Value mismatch. Expected %s but found null", expected);
		} else if (input.hasValueOfType(ArrayValue.class, name)) {
			ArrayValue arrayValue = input.getValue(ArrayValue.class, name);
			if (arrayValue != null) {
				for (BaseValue<?, ?, ?> value : arrayValue.getValue()) {
					@SuppressWarnings("unchecked")
					T observedValue = (T) value.getValue();
					String observed = format(observedValue);
					String expected = format(expectedValue);

					if (observed.equals(expected)) {
						return ActionResult.ok();
					}
				}
			}
		}

		return ActionResult.error("Input has no value named %s of type %s",
								  name, expectedType.getName());
	}

}
