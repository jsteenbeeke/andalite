package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.annotation.LongValue;

public class LongArrayValueCondition extends ArrayValueCondition<Long> {
	public LongArrayValueCondition(@Nonnull final String name,
			@Nonnull final Long... values) {
		super(name, LongValue.class, values);
	}
}
