package com.jeroensteenbeeke.andalite.analyzer.statements;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;

public class ExpressionStatement extends AnalyzedStatement {
	public ExpressionStatement(Location location, AnalyzedExpression expression) {
		super(location);
		this.expression = expression;
	}

	private final AnalyzedExpression expression;

	@Override
	public String toJavaString() {
		return expression.toJavaString();
	}

}
