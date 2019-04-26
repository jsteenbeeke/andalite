package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import com.google.common.collect.ImmutableMap;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NormalAnnotationExpression extends AnnotationExpression {
	private final ImmutableMap<String, AnalyzedExpression> values;

	public NormalAnnotationExpression(@Nonnull Location location, @Nonnull String qualifiedName) {
		this(location, qualifiedName, ImmutableMap.of());
	}

	private NormalAnnotationExpression(@Nonnull Location location, @Nonnull String qualifiedName, @Nonnull ImmutableMap<String, AnalyzedExpression> values) {
		super(location, qualifiedName);
		this.values = values;
	}

	public Map<String, AnalyzedExpression> getValues() {
		return values;
	}

	public NormalAnnotationExpression addValue(@Nonnull String label, @Nonnull AnalyzedExpression value) {
		return new NormalAnnotationExpression(getLocation(), getQualifiedName(), ImmutableMap
			.<String, AnalyzedExpression>builder()
			.putAll(values)
			.put(label, value)
			.build());
	}

	@Override
	public String toJavaString() {
		return String.format("@%s(%s)", getQualifiedName(), values
			.entrySet()
			.stream()
			.map(e -> String.format("%s = %s", e.getKey(), e.getValue().toJavaString()))
			.collect(Collectors
						 .joining(", ")));
	}
}
