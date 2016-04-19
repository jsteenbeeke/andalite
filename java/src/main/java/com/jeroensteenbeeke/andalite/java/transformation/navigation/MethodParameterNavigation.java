package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;

public class MethodParameterNavigation extends
		AbstractParameterNavigation<AnalyzedMethod> {
	public MethodParameterNavigation(IJavaNavigation<AnalyzedMethod> chained,
			String type, String name) {
		super(chained, type, name);
	}

	@Override
	public AnalyzedParameter navigate(AnalyzedMethod chainedTarget)
			throws NavigationException {
		for (AnalyzedParameter analyzedParameter : chainedTarget
				.getParameters()) {
			if (name.equals(analyzedParameter.getName())
					&& type.equals(analyzedParameter.getType())) {
				return analyzedParameter;
			}
		}

		throw new NavigationException(
				"Method %s has no parameter named %s of type %s",
				chainedTarget.getName(), name, type);
	}
}
