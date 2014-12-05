package com.jeroensteenbeeke.andalite.transformation.navigation;

import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;

public class PublicClassNavigation implements Navigation<AnalyzedClass> {

	@Override
	public AnalyzedClass navigate(AnalyzedSourceFile file)
			throws NavigationException {
		for (AnalyzedClass analyzedClass : file.getClasses()) {
			if (analyzedClass.getAccessModifier() == AccessModifier.PUBLIC) {
				return analyzedClass;
			}
		}

		throw new NavigationException("Source file has no public class");
	}

	@Override
	public String getDescription() {
		return "Public Class";
	}

}
