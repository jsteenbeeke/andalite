package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.annotation.BooleanValue;

public class BooleanArrayValueCondition extends ArrayValueCondition<Boolean> {
	public BooleanArrayValueCondition(@Nonnull final String name,
			@Nonnull final Boolean... values) {
		super(name, BooleanValue.class, values);
	}
}
