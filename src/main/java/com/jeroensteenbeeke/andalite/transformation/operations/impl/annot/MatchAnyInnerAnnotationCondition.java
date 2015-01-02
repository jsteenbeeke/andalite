package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;

public final class MatchAnyInnerAnnotationCondition implements
		InnerAnnotationCondition {

	@Override
	public boolean isSatisfiedBy(AnnotationValue value) {
		return true;
	}

}
