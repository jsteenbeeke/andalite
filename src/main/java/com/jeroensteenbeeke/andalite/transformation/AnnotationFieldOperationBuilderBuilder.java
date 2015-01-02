package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.transformation.navigation.AnnotationFieldNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.annot.InnerAnnotationCondition;

public class AnnotationFieldOperationBuilderBuilder {

	final StepCollector collector;

	final Navigation<AnalyzedAnnotation> navigation;

	final String name;

	public AnnotationFieldOperationBuilderBuilder(StepCollector collector,
			Navigation<AnalyzedAnnotation> navigation, String name) {
		this.collector = collector;
		this.navigation = navigation;
		this.name = name;
	}

	public NavigateToInnerAnnotationConditionBuilder inArray() {
		return new NavigateToInnerAnnotationConditionBuilder(this);
	}

	public AnnotationFieldOperationBuilder ifNotAnArray() {
		return new AnnotationFieldOperationBuilder(collector,
				new AnnotationFieldNavigation(navigation, name, null));
	}

	public AnnotationFieldOperationBuilder getBuilderForCondition(
			InnerAnnotationCondition finalCondition) {

		return new AnnotationFieldOperationBuilder(collector,
				new AnnotationFieldNavigation(navigation, name, finalCondition));
	}

}
