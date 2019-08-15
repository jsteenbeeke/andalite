package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import com.jeroensteenbeeke.andalite.core.Location;

import javax.annotation.Nonnull;

public class MarkerAnnotationExpression extends AnnotationExpression {

	public MarkerAnnotationExpression(@Nonnull Location location, @Nonnull String qualifiedName) {
		super(location, qualifiedName);
	}

	@Override
	public String toJavaString() {
		return String.format("@%s", getQualifiedName());
	}
}
