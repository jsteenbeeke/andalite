package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.transformation.navigation.FieldNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.FieldOperation;

public class FieldOperationBuilder implements
		ScopedOperationBuilder<AnalyzedField, FieldOperation> {
	private final StepCollector collector;

	private final Navigation<AnalyzedField> navigation;

	FieldOperationBuilder(StepCollector collector,
			Navigation<AnalyzedClass> parentNav, String fieldName) {
		this.collector = collector;
		this.navigation = new FieldNavigation(parentNav, fieldName);
	}

	@Override
	public void ensure(FieldOperation operation) {
		collector.addStep(navigation, operation);
	}

}
