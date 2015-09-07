package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;

public class EnsureStatementComment<S extends AnalyzedStatement> implements
		IJavaOperation<S> {

	private final String comment;

	private final boolean prefix;

	public EnsureStatementComment(String comment, boolean prefix) {
		super();
		this.comment = comment;
		this.prefix = prefix;
	}

	@Override
	public List<Transformation> perform(S input) throws OperationException {
		if (!input.getComments().contains(input)) {
			if (prefix) {
				return ImmutableList.of(Transformation.insertBefore(input,
						String.format("// %s\n", comment)));
			} else {
				return ImmutableList.of(Transformation.insertAfter(input,
						String.format("// %s\n", comment)));
			}
		}

		return ImmutableList.of();
	}

	@Override
	public String getDescription() {
		return "Ensure statement has comment: ".concat(comment);
	}

}
