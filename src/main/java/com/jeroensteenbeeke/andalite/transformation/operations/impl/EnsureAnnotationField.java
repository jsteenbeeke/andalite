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

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.AnnotationOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public class EnsureAnnotationField<T extends BaseValue<T>> implements
		AnnotationOperation {
	private final String name;

	private final T expectedValue;

	public EnsureAnnotationField(String name, T value) {
		super();
		this.name = name;
		this.expectedValue = value;
	}

	@Override
	public List<Transformation> perform(AnalyzedAnnotation input)
			throws OperationException {
		final String actualName = name != null ? name : "value";

		@SuppressWarnings("unchecked")
		Class<T> expectedType = (Class<T>) expectedValue.getClass();
		if (input.hasValueOfType(expectedType, name)) {
			T value = input.getValue(expectedType, name);

			if (!Objects.equals(value, expectedValue)) {
				throw new OperationException(
						"Annotation field already has value, but does not match expected value, and replacement not yet supported");
			}
		} else {
			Location parametersLocation = input.getParametersLocation();

			if (parametersLocation == null) {
				return ImmutableList.of(Transformation.insertAfter(
						input,
						String.format("(%s=%s)", actualName,
								expectedValue.toJava())));
			} else {
				// TODO determine if parameters exist
				String postfix = ",";

				// TODO fix location
				return ImmutableList.of(Transformation.insertBefore(
						parametersLocation,
						String.format("%s=%s%s", actualName,
								expectedValue.toJava(), postfix)));
			}
		}

		return ImmutableList.of();
	}

}
