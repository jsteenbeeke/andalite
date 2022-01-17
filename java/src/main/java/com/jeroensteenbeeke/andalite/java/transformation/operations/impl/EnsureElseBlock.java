package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BaseStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.IfStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IStatementOperation;
import com.jeroensteenbeeke.lux.ActionResult;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class EnsureElseBlock implements IStatementOperation<IfStatement, BaseStatement.BaseStatementInsertionPoint> {
	@Override
	public List<Transformation> perform(@NotNull IfStatement input) throws OperationException {
		if (input.getElseStatement() == null) {
			return ImmutableList.of(input.insertAt(BaseStatement.BaseStatementInsertionPoint.AFTER, "else {}"));
		}

		return ImmutableList.of();
	}

	@Override
	public ActionResult verify(@NotNull IfStatement input) {
		if (input.getElseStatement() == null) {
			return ActionResult.error("Statement does not have an else statement");
		} else {
			return ActionResult.ok();
		}
	}

	@Override
	public String getDescription() {
		return "ensure if statement has an else block";
	}
}
