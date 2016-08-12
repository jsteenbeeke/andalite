package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;

public class RemoveMethodAnnotation extends
		AbstractRemoveAnnotation<AnalyzedMethod> implements IMethodOperation {

	public RemoveMethodAnnotation(String annotation) {
		super(annotation);
	}

}
