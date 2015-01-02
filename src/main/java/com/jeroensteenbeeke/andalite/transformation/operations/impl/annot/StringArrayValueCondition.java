package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.annotation.StringValue;

public class StringArrayValueCondition extends ArrayValueCondition<String> {
	public StringArrayValueCondition(@Nonnull final String name,
			@Nonnull final String... values) {
		super(name, StringValue.class, values);
	}
}
