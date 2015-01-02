package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.annotation.DoubleValue;

public class DoubleValueCondition extends BaseValueCondition<Double> {
	public DoubleValueCondition(@Nullable final String name,
			@Nullable final Double value) {
		super(name, DoubleValue.class, value);
	}
}
