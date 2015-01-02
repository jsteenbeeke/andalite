package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.annotation.IntegerValue;

public class IntegerValueCondition extends BaseValueCondition<Integer> {
	public IntegerValueCondition(@Nullable final String name,
			@Nullable final Integer value) {
		super(name, IntegerValue.class, value);
	}
}
