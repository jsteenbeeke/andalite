package com.jeroensteenbeeke.andalite.analyzer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedField extends AccessModifiable {
	private final String name;

	private final String type;

	public AnalyzedField(@Nonnull Location location, int modifiers,
			@Nonnull String name, @Nonnull String type) {
		super(location, modifiers);
		this.name = name;
		this.type = type;
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public String getType() {
		return type;
	}

	@Override
	public void onModifierOutputted(OutputCallback callback) {
		callback.write(type);
		callback.write(" ");
		callback.write(name);
		callback.write(";");
	}
}
