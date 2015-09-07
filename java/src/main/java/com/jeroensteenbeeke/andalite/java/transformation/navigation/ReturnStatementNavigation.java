package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;

public class ReturnStatementNavigation extends
		ChainedNavigation<IBodyContainer, ReturnStatement> {
	public ReturnStatementNavigation(IJavaNavigation<IBodyContainer> chained) {
		super(chained);
	}

	@Override
	public ReturnStatement navigate(IBodyContainer chainedTarget)
			throws NavigationException {
		return (ReturnStatement) chainedTarget
				.getStatements()
				.stream()
				.filter(s -> s instanceof ReturnStatement)
				.findAny()
				.orElseThrow(
						() -> new NavigationException(
								"No return statement in block"));
	}

	@Override
	public String getStepDescription() {
		return " find return statement in block";
	}

}
