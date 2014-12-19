package com.jeroensteenbeeke.andalite.analyzer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;

public abstract class Denomination extends AccessModifiable {

	private final String packageName;

	private final String denominationName;

	public Denomination(@Nonnull Location location, int modifiers,
			@Nonnull String packageName, @Nonnull String denominationName) {
		super(location, modifiers);
		this.packageName = packageName;
		this.denominationName = denominationName;

	}

	public String getDenominationName() {
		return denominationName;
	}

	public String getPackageName() {
		return packageName;
	}
}
