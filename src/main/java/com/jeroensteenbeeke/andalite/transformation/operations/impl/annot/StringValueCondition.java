package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.annotation.StringValue;

public class StringValueCondition extends BaseValueCondition<String> {
	public StringValueCondition(@Nullable final String name,
			@Nullable final String value) {
		super(name, StringValue.class, value);
	}
}
