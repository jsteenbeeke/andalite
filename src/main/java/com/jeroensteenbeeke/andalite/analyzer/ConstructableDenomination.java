package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;

public abstract class ConstructableDenomination extends ContainingDenomination {

	private final List<AnalyzedConstructor> constructors;

	protected ConstructableDenomination(Location location, int modifiers,
			String packageName, String denominationName) {
		super(location, modifiers, packageName, denominationName);
		this.constructors = Lists.newArrayList();
	}

	void addConstructor(@Nonnull AnalyzedConstructor constructor) {
		this.constructors.add(constructor);
	}

	@Nonnull
	public List<AnalyzedConstructor> getConstructors() {
		return ImmutableList.copyOf(constructors);
	}

}
