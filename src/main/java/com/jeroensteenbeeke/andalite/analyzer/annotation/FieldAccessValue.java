package com.jeroensteenbeeke.andalite.analyzer.annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.IOutputCallback;

public class FieldAccessValue extends BaseValue<String> {
	public FieldAccessValue(@Nonnull Location location, @Nullable String name,
			@Nullable String scope, @Nullable String value) {
		super(location, name, createValue(scope, value));
	}

	@Override
	public void output(IOutputCallback callback) {
		String value = getValue();

		if (value != null) {
			callback.write(value);
		} else {
			callback.write(null);
		}

	}

	private static String createValue(@Nullable String scope,
			@Nullable String value) {
		if (scope != null && value != null) {
			return String.format("%s.%s", scope, value);
		}

		return null;
	}
}
