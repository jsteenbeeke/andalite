package com.jeroensteenbeeke.andalite.analyzer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;

public class AnalyzedParameter extends Annotatable {
	private final String type;

	private final String name;

	public AnalyzedParameter(@Nonnull Location location, @Nonnull String type,
			@Nonnull String name) {
		super(location);
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Override
	public boolean isPrintNewlineAfterAnnotation() {
		return false;
	}

	@Override
	public void onOutput(OutputCallback callback) {
		callback.write(type);
		callback.write(" ");
		callback.write(name);
	}
}
