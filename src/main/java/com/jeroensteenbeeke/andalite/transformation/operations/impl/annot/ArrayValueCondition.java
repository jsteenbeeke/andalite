package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import java.util.Set;

import com.google.common.collect.Sets;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;

public abstract class ArrayValueCondition<T> implements
		InnerAnnotationCondition {
	private final String name;

	private final Class<? extends BaseValue<T>> expectedType;

	private final T[] expectedValue;

	protected ArrayValueCondition(String name,
			Class<? extends BaseValue<T>> expectedType, T[] expectedValue) {
		super();
		this.name = name;
		this.expectedType = expectedType;
		this.expectedValue = expectedValue;
	}

	@Override
	public boolean isSatisfiedBy(AnnotationValue value) {
		AnalyzedAnnotation annotation = value.getValue();

		if (annotation != null) {
			if (annotation.hasValueOfType(ArrayValue.class, name)) {
				ArrayValue arrayValue = annotation.getValue(ArrayValue.class,
						name);

				Set<T> required = Sets.newHashSet(expectedValue);

				for (BaseValue<?> baseValue : arrayValue.getValue()) {
					if (!expectedType.isAssignableFrom(baseValue.getClass())) {
						return false;
					} else {
						required.remove(baseValue.getValue());
					}
				}

				return required.isEmpty();
			}
		}

		return false;
	}
}
