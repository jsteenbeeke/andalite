package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.annotation.BooleanValue;

public class BooleanValueCondition extends BaseValueCondition<Boolean> {
	public BooleanValueCondition(@Nullable final String name,
			@Nullable final Boolean value) {
		super(name, BooleanValue.class, value);
	}
}
