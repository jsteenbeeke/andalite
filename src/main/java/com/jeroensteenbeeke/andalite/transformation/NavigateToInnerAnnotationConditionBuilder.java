package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.transformation.operations.impl.InnerAnnotationConditionBuilder;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.annot.InnerAnnotationCondition;

public class NavigateToInnerAnnotationConditionBuilder
		extends
		InnerAnnotationConditionBuilder<NavigateToInnerAnnotationConditionBuilder, AnnotationFieldOperationBuilder> {

	private final AnnotationFieldOperationBuilderBuilder parent;

	public NavigateToInnerAnnotationConditionBuilder(
			AnnotationFieldOperationBuilderBuilder parent) {

		this.parent = parent;
	}

	@Override
	public AnnotationFieldOperationBuilder getReturnObject(
			InnerAnnotationCondition finalCondition) {
		return parent.getBuilderForCondition(finalCondition);
	}
}
