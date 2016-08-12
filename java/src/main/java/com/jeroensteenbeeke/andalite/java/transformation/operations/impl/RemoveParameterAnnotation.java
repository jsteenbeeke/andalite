package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IParameterOperation;

public class RemoveParameterAnnotation
		extends AbstractRemoveAnnotation<AnalyzedParameter>
		implements IParameterOperation {

	public RemoveParameterAnnotation(String annotation) {
		super(annotation);
	}

}
