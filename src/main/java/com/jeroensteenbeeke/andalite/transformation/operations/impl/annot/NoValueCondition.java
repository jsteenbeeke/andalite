package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;

public class NoValueCondition implements InnerAnnotationCondition {
	private final String name;

	public NoValueCondition(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean isSatisfiedBy(AnnotationValue value) {
		return !value.getValue().hasValueNamed(name);
	}

	@Override
	public String toString() {
		return String.format("Does not have a value '%s'", name);
	}

}
