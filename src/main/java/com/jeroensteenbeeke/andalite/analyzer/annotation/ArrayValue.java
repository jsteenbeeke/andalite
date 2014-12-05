package com.jeroensteenbeeke.andalite.analyzer.annotation;

import java.util.List;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.OutputCallback;

public final class ArrayValue extends BaseValue<List<BaseValue<?>>> {

	public ArrayValue(@Nonnull String name, @Nonnull List<BaseValue<?>> value) {
		super(name, value);
	}

	@Override
	public void output(OutputCallback callback) {
		callback.write("{ ");

		int i = 0;
		for (BaseValue<?> baseValue : getValue()) {
			if (i++ > 0) {
				callback.write(", ");
			}

			baseValue.output(callback);
		}

		callback.write(" }");
	}
}
