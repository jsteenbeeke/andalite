package com.jeroensteenbeeke.andalite.transformation.navigation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.analyzer.IBodyContainer;
import com.jeroensteenbeeke.andalite.analyzer.statements.IfStatement;

public class ByExpressionIfStatementNavigation extends
		ChainedNavigation<IBodyContainer, IfStatement> {
	private final String expression;

	public ByExpressionIfStatementNavigation(
			INavigation<IBodyContainer> chained, String expression) {
		super(chained);
		this.expression = expression;
	}

	@Override
	public IfStatement navigate(IBodyContainer chainedTarget)
			throws NavigationException {
		for (AnalyzedStatement analyzedStatement : chainedTarget
				.getStatements()) {
			if (analyzedStatement instanceof IfStatement) {
				IfStatement stmt = (IfStatement) analyzedStatement;
				AnalyzedExpression condition = stmt.getCondition();
				if (condition != null
						&& expression.equals(condition.toJavaString())) {
					return stmt;
				}

			}
		}

		throw new NavigationException(
				"No if statement with expression %s in body", expression);
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return String.format("if-statement with expression %s", expression);
	}

}
