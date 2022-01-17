package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import com.jeroensteenbeeke.andalite.core.Location;

import org.jetbrains.annotations.NotNull;

public class MarkerAnnotationExpression extends AnnotationExpression {

	public MarkerAnnotationExpression(@NotNull Location location, @NotNull String qualifiedName) {
		super(location, qualifiedName);
	}

	@Override
	public String toJavaString() {
		return String.format("@%s", getQualifiedName());
	}
}
