package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;

public abstract class BaseValueCondition<T> implements InnerAnnotationCondition {
	private final String name;

	private final Class<? extends BaseValue<T>> expectedType;

	private final T expectedValue;

	protected BaseValueCondition(@Nullable String name,
			@Nonnull Class<? extends BaseValue<T>> expectedType,
			@Nullable T expectedValue) {
		super();
		this.name = name;
		this.expectedType = expectedType;
		this.expectedValue = expectedValue;
	}

	@Override
	public boolean isSatisfiedBy(AnnotationValue value) {
		AnalyzedAnnotation annotation = value.getValue();

		if (annotation != null) {
			if (annotation.hasValueOfType(expectedType, name)) {
				BaseValue<T> baseValue = annotation
						.getValue(expectedType, name);

				return Objects.equals(baseValue.getValue(), expectedValue);
			}
		}

		return false;
	}

}
