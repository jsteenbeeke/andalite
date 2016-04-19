package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;

public class ConstructorParameterNavigation extends
		AbstractParameterNavigation<AnalyzedConstructor> {
	public ConstructorParameterNavigation(
			IJavaNavigation<AnalyzedConstructor> chained, String type,
			String name) {
		super(chained, type, name);
	}

	@Override
	public AnalyzedParameter navigate(AnalyzedConstructor chainedTarget)
			throws NavigationException {
		for (AnalyzedParameter analyzedParameter : chainedTarget
				.getParameters()) {
			if (name.equals(analyzedParameter.getName())
					&& type.equals(analyzedParameter.getType())) {
				return analyzedParameter;
			}
		}

		throw new NavigationException(
				"Constructor has no parameter named %s of type %s", name, type);
	}
}
