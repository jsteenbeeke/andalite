package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;

public class EnsureClassAnnotation extends
		AbstractEnsureAnnotation<AnalyzedClass> implements ClassOperation {
	public EnsureClassAnnotation(String type) {
		super(type);

	}

}
