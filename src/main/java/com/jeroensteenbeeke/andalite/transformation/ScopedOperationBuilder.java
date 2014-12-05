package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.Locatable;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;

public interface ScopedOperationBuilder<T extends Locatable, O extends Operation<T>> {
	public void ensure(O operation);
}
