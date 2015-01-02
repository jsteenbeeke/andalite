package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;

public interface InnerAnnotationCondition {
	boolean isSatisfiedBy(AnnotationValue value);
}
