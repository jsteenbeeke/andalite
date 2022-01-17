package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import com.google.common.collect.ImmutableMap;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NormalAnnotationExpression extends AnnotationExpression {
	private final ImmutableMap<String, AnalyzedExpression> values;

	public NormalAnnotationExpression(@NotNull Location location, @NotNull String qualifiedName) {
		this(location, qualifiedName, ImmutableMap.of());
	}

	private NormalAnnotationExpression(@NotNull Location location, @NotNull String qualifiedName, @NotNull ImmutableMap<String, AnalyzedExpression> values) {
		super(location, qualifiedName);
		this.values = values;
	}

	public Map<String, AnalyzedExpression> getValues() {
		return values;
	}

	public NormalAnnotationExpression addValue(@NotNull String label, @NotNull AnalyzedExpression value) {
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
