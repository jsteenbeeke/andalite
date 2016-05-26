package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;

public class ByIndexConstructorParameterNavigation extends
		AbstractByIndexParameterNavigation<AnalyzedConstructor> {

	public ByIndexConstructorParameterNavigation(
			IJavaNavigation<AnalyzedConstructor> chained, int index) {
		super(chained, index);
	}

}
