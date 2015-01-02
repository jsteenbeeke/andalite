package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.annotation.CharValue;

public class CharValueCondition extends BaseValueCondition<Character> {
	public CharValueCondition(@Nullable final String name,
			@Nullable final Character value) {
		super(name, CharValue.class, value);
	}
}
