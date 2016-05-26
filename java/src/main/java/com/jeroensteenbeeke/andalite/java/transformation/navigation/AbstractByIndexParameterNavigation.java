package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import java.util.List;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.IParameterized;

public abstract class AbstractByIndexParameterNavigation<From extends IBodyContainer & IParameterized>
		extends ChainedNavigation<From, AnalyzedParameter> {
	private final int index;

	protected AbstractByIndexParameterNavigation(IJavaNavigation<From> chained,
			int index) {
		super(chained);
		this.index = index;
	}

	@Override
	public AnalyzedParameter navigate(From chainedTarget)
			throws NavigationException {
		List<AnalyzedParameter> parameters = chainedTarget.getParameters();

		if (parameters.size() > index) {
			return parameters.get(index);
		}

		throw new NavigationException(
				"Invalid parameter index %d, only %d parameters present",
				index, parameters.size());
	}

	@Override
	public String getStepDescription() {
		return String.format("Go to parameter with 0-based index %d", index);
	}
}
