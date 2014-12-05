package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.Annotatable;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public abstract class AbstractEnsureAnnotation<T extends Annotatable>
		implements Operation<T> {
	private final String type;

	public AbstractEnsureAnnotation(String type) {
		super();
		this.type = type;
	}

	@Override
	public final List<Transformation> perform(T input)
			throws OperationException {
		if (!input.hasAnnotation(type)) {
			return ImmutableList.of(Transformation.insertBefore(input, "@"
					.concat(type).concat("\n")));
		}

		return ImmutableList.of();
	}
}