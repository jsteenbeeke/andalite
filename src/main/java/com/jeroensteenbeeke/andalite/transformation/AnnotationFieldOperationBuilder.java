package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.AnnotationOperation;

public class AnnotationFieldOperationBuilder extends
		AbstractOperationBuilder<AnalyzedAnnotation, AnnotationOperation> {
	public AnnotationFieldOperationBuilder(StepCollector collector,
			Navigation<AnalyzedAnnotation> navigation) {
		super(collector, navigation);
	}

}
