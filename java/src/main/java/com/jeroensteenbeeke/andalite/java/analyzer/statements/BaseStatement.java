package com.jeroensteenbeeke.andalite.java.analyzer.statements;

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;

public abstract class BaseStatement extends AnalyzedStatement<BaseStatement, BaseStatement.BaseStatementInsertionPoint> {
	public BaseStatement(Location location) {
		super(location);
	}

	@Override
	public BaseStatementInsertionPoint getBeforeInsertionPoint() {
		return BaseStatementInsertionPoint.BEFORE;
	}

	@Override
	public BaseStatementInsertionPoint getAfterInsertionPoint() {
		return BaseStatementInsertionPoint.AFTER;
	}

	public enum BaseStatementInsertionPoint implements IInsertionPoint<BaseStatement> {
		BEFORE {
			@Override
			public int position(BaseStatement container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(BaseStatement container) {
				return container.getLocation().getEnd();
			}
		};
	}
}
