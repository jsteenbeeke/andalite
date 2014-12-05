package com.jeroensteenbeeke.andalite.transformation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.PackageClassNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.PublicClassNavigation;

public class ClassLocator {
	private final Navigation<AnalyzedClass> navigation;

	private ClassLocator(@Nonnull Navigation<AnalyzedClass> navigation) {
		this.navigation = navigation;
	}

	@Nonnull
	Navigation<AnalyzedClass> getNavigation() {
		return navigation;
	}

	@Nonnull
	public static ClassLocator publicClass() {
		return new ClassLocator(new PublicClassNavigation());
	}

	@Nonnull
	public static ClassLocator packageClassNamed(@Nonnull String name) {
		return new ClassLocator(new PackageClassNavigation(name));
	}
}
