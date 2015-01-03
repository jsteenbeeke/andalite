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
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.AnnotationOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public abstract class EnsureAnnotationField<T> implements AnnotationOperation {
	private final String name;

	private final T expectedValue;

	private final Class<? extends BaseValue<T>> expectedType;

	public EnsureAnnotationField(@Nonnull String name,
			@Nonnull Class<? extends BaseValue<T>> expectedType,
			@Nonnull T value) {
		super();
		this.name = name;
		this.expectedType = expectedType;
		this.expectedValue = value;
	}

	@Override
	public List<Transformation> perform(AnalyzedAnnotation input)
			throws OperationException {
		final String actualName = name != null ? name : "value";

		if (input.hasValueOfType(expectedType, name)) {
			BaseValue<T> value = input.getValue(expectedType, name);

			if (value != null
					&& !Objects.equals(value.getValue(), expectedValue)) {
				return ImmutableList.of(Transformation.replace(value, String
						.format("%s=%s", actualName, format(expectedValue))));
			}
		} else if (input.hasValueOfType(ArrayValue.class, name)) {
			ArrayValue value = input.getValue(ArrayValue.class, name);

			boolean present = false;

			BaseValue<?> last = null;

			for (BaseValue<?> baseValue : value.getValue()) {
				if (Objects.equals(baseValue.getValue(), expectedValue)) {
					present = true;
				}
				last = baseValue;
			}

			if (!present) {
				if (last != null) {
					return ImmutableList.of(Transformation.insertAfter(last,
							String.format(", %s", format(expectedValue))));
				} else {
					return ImmutableList.of(Transformation.replace(value,
							String.format("{%s}", format(expectedValue))));
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
						String.format("%s%s=%s%s", openParen, actualName,
								format(expectedValue), closeParen)));
			} else {
				String postfix = input.hasValues() ? ", " : "";

				final int start = parametersLocation.getStart()
						+ Math.max(1, openParen.length());

				return ImmutableList.of(Transformation.insertAt(start, String
						.format("%s%s=%s%s%s", openParen, actualName,
								format(expectedValue), postfix, closeParen)));
			}
		}

		return ImmutableList.of();
	}

	public abstract String format(T value);

	@Override
	public String getDescription() {
		return String.format("presence of annotation field %s with value %s",
				name, format(expectedValue));
	}

}
