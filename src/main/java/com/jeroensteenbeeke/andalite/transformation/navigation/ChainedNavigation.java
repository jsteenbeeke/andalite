package com.jeroensteenbeeke.andalite.transformation.navigation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.Locatable;

public abstract class ChainedNavigation<From extends Locatable, To extends Locatable>
		implements Navigation<To> {
	private final Navigation<From> chained;

	protected ChainedNavigation(Navigation<From> chained) {
		super();
		this.chained = chained;
	}

	@Override
	public final To navigate(AnalyzedSourceFile file)
			throws NavigationException {
		From chainedTarget = chained.navigate(file);

		return navigate(chainedTarget);
	}

	public abstract To navigate(From chainedTarget) throws NavigationException;
}
