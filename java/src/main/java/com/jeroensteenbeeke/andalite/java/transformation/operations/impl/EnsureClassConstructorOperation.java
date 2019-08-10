package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;

import java.util.List;

public class EnsureClassConstructorOperation extends EnsureConstructorOperation<AnalyzedClass, AnalyzedClass.ClassInsertionPoint> implements
	IClassOperation {
	public EnsureClassConstructorOperation(AccessModifier modifier,
		List<ParameterDescriptor> parameters) {
		super(modifier, parameters);
	}

	@Override
	public AnalyzedClass.ClassInsertionPoint getInsertionPoint() {
		return AnalyzedClass.ClassInsertionPoint.AFTER_LAST_FIELD;
	}
}
