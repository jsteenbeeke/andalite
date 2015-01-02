package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import com.jeroensteenbeeke.andalite.transformation.operations.impl.annot.InnerAnnotationCondition;

public class EnsureInnerAnnotationConditionBuilder
		extends
		InnerAnnotationConditionBuilder<EnsureInnerAnnotationConditionBuilder, EnsureInnerAnnotationField> {

	private final EnsureInnerAnnotationField returnObject;

	EnsureInnerAnnotationConditionBuilder(
			EnsureInnerAnnotationField returnObject) {
		this.returnObject = returnObject;
	}

	@Override
	public EnsureInnerAnnotationField getReturnObject(
			InnerAnnotationCondition finalCondition) {
		return returnObject.setCondition(finalCondition);
	}
}
