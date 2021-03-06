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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot;

import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BaseValue;

public abstract class ArrayValueCondition<T> implements
	InnerAnnotationCondition {
	private final String name;

	private final Class<? extends BaseValue<T, ?, ?>> expectedType;

	private final T[] expectedValue;

	protected ArrayValueCondition(String name,
								  Class<? extends BaseValue<T, ?, ?>> expectedType, T[] expectedValue) {
		super();
		this.name = name;
		this.expectedType = expectedType;
		this.expectedValue = expectedValue;
	}

	@Override
	public boolean test(AnnotationValue value) {
		AnalyzedAnnotation annotation = value.getValue();

		ArrayValue arrayValue = annotation.getValue(ArrayValue.class,
													name);
		if (arrayValue != null) {

			Set<T> required = Sets.newHashSet(expectedValue);

			for (BaseValue<?, ?, ?> baseValue : arrayValue.getValue()) {
				if (!expectedType.isAssignableFrom(baseValue.getClass())) {
					return false;
				} else {
					@SuppressWarnings("unchecked")
					T actualValue = (T) baseValue.getValue();
					required.remove(actualValue);
				}
			}

			return required.isEmpty();
		} else if (expectedValue.length == 1
			&& annotation.hasValueOfType(expectedType, name)) {
			return annotation.getValue(expectedType, name).getValue()
							 .equals(expectedValue[0]);
		}

		return false;
	}

	@Override
	public String toString() {
		return String.format("%s has values: %s", name,
							 Joiner.on(", ").join(expectedValue));
	}
}
