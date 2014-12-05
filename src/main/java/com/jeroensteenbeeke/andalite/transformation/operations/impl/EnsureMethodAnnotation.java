package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.transformation.operations.MethodOperation;

public class EnsureMethodAnnotation extends
		AbstractEnsureAnnotation<AnalyzedMethod> implements MethodOperation {
	public EnsureMethodAnnotation(String type) {
		super(type);
	}

}
