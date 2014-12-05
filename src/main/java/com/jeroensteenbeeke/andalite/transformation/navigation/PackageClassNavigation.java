package com.jeroensteenbeeke.andalite.transformation.navigation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;

public class PackageClassNavigation implements Navigation<AnalyzedClass> {
	private final String name;

	public PackageClassNavigation(@Nonnull String name) {
		this.name = name;
	}

	@Override
	@Nonnull
	public AnalyzedClass navigate(AnalyzedSourceFile file)
			throws NavigationException {
		for (AnalyzedClass analyzedClass : file.getClasses()) {
			if (analyzedClass.getAccessModifier() == AccessModifier.DEFAULT
					&& name.equals(analyzedClass.getClassName())) {
				return analyzedClass;
			}
		}

		throw new NavigationException(
				"Source file has no package class named %s", name);
	}

	@Override
	@Nonnull
	public String getDescription() {
		return String.format("Go to package class %s", name);
	}

}
