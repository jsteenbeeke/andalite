package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.transformation.operations.FieldOperation;

public class EnsureFieldAnnotation extends
		AbstractEnsureAnnotation<AnalyzedField> implements FieldOperation {
	public EnsureFieldAnnotation(String type) {
		super(type);
	}

}
