package com.jeroensteenbeeke.andalite.transformation.operations.impl.annot;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.annotation.DoubleValue;

public class DoubleArrayValueCondition extends ArrayValueCondition<Double> {
	public DoubleArrayValueCondition(@Nonnull final String name,
			@Nonnull final Double... values) {
		super(name, DoubleValue.class, values);
	}
}
