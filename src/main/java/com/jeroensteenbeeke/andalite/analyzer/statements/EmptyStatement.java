package com.jeroensteenbeeke.andalite.analyzer.statements;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;

public class EmptyStatement extends AnalyzedStatement {

	public EmptyStatement(Location location) {
		super(location);
	}

	@Override
	public String toJavaString() {
		return "";
	}

}
