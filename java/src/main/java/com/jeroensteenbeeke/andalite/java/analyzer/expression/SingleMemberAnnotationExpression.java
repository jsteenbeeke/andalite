package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;

public class SingleMemberAnnotationExpression extends AnnotationExpression {
	private final AnalyzedExpression member;

	public SingleMemberAnnotationExpression(Location location, String qualifiedName, AnalyzedExpression member) {
		super(location, qualifiedName);

		this.member = member;
	}

	@Override
	public String toJavaString() {
		return String.format("@%s(%s)", getQualifiedName(), member.toJavaString());
	}
}
