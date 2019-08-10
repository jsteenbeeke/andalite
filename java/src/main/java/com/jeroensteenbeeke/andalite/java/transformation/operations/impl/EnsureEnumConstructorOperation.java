package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;

import java.util.List;

public class EnsureEnumConstructorOperation extends EnsureConstructorOperation<AnalyzedEnum, AnalyzedEnum.EnumInsertionPoint> implements
	IEnumOperation {
	public EnsureEnumConstructorOperation(AccessModifier modifier,
		List<ParameterDescriptor> parameters) {
		super(modifier, parameters);
	}

	@Override
	public AnalyzedEnum.EnumInsertionPoint getInsertionPoint() {
		return AnalyzedEnum.EnumInsertionPoint.START_OF_IMPLEMENTATION;
	}
}
