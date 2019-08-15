package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.SwitchEntryStatement;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;

public class SwitchExpression extends AnalyzedExpression {
	private final AnalyzedExpression selector;

	private final ImmutableList<SwitchEntryStatement> statements;

	public SwitchExpression(Location location, AnalyzedExpression selector) {
		this(location, selector, ImmutableList.of());
	}

	private SwitchExpression(Location location, AnalyzedExpression selector, ImmutableList<SwitchEntryStatement> statements) {
		super(location);
		this.selector = selector;
		this.statements = statements;
	}

	public SwitchExpression addStatement(@Nonnull SwitchEntryStatement statement) {
		return new SwitchExpression(getLocation(), selector, ImmutableList.<SwitchEntryStatement>builder()
			.addAll(statements)
			.add(statement)
			.build());
	}

	@Override
	public String toJavaString() {
		return String.format("switch (%s) {\n\t%s\n}", selector.toJavaString(),
							 statements.stream().map(s ->
														 String.format("%s -> %s", Optional
																		   .ofNullable(s.getValue())
																		   .map(AnalyzedExpression::toJavaString)
																		   .orElse("default"),
																	   s
																		   .getStatements()
																		   .stream()
																		   .map(AnalyzedStatement::toJavaString)
																		   .collect(Collectors.joining("; ")))


							 ).collect(Collectors.joining(";\n\t")));
	}
}
