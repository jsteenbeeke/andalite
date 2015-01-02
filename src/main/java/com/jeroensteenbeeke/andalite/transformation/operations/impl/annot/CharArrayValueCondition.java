package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.annotation.IntegerValue;

public class CharArrayValueCondition extends ArrayValueCondition<Integer> {
	public CharArrayValueCondition(@Nonnull final String name,
			@Nonnull final Integer... values) {
		super(name, IntegerValue.class, values);
	}
}
