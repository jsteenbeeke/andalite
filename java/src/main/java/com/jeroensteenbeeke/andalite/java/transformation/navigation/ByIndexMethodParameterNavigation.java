package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;

public class ByIndexMethodParameterNavigation extends
		AbstractByIndexParameterNavigation<AnalyzedMethod> {

	public ByIndexMethodParameterNavigation(
			IJavaNavigation<AnalyzedMethod> chained, int index) {
		super(chained, index);
	}

}
