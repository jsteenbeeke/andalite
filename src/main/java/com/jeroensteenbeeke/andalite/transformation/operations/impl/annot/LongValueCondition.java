package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.annotation.LongValue;

public class LongValueCondition extends BaseValueCondition<Long> {
	public LongValueCondition(@Nullable final String name,
			@Nullable final Long value) {
		super(name, LongValue.class, value);
	}
}
