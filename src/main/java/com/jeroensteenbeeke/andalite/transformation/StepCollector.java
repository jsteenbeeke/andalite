package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.Locatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;

public interface StepCollector {
	<T extends Locatable> void addStep(Navigation<T> navigation,
			Operation<T> operation);
}
