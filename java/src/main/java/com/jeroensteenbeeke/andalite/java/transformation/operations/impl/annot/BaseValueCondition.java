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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

public abstract class BaseValueCondition<T> implements InnerAnnotationCondition {
	private final String name;

	private final Class<? extends BaseValue<T,?,?>> expectedType;

	private final T expectedValue;

	protected BaseValueCondition(@Nullable String name,
			@NotNull Class<? extends BaseValue<T,?,?>> expectedType,
			@Nullable T expectedValue) {
		super();
		this.name = name;
		this.expectedType = expectedType;
		this.expectedValue = expectedValue;
	}

	@Override
	public boolean test(AnnotationValue value) {
		AnalyzedAnnotation annotation = value.getValue();

		if (annotation.hasValueOfType(expectedType, name)) {
			BaseValue<T,?,?> baseValue = annotation
					.getValue(expectedType, name);

			return Objects.equals(baseValue.getValue(), expectedValue);
		}

		return false;
	}

	@Override
	public String toString() {
		return String.format("%s has value %s", name, expectedValue);
	}

}
