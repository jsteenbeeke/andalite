package com.jeroensteenbeeke.andalite.analyzer.annotation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.OutputCallback;

public final class BooleanValue extends BaseValue<Boolean> {

	public BooleanValue(@Nonnull String name, boolean value) {
		super(name, value);
	}

	@Override
	public void output(OutputCallback callback) {
		callback.write(Boolean.toString(getValue()));

	}
}
