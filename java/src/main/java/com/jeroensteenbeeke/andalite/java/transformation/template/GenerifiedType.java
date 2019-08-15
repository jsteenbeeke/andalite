package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.google.common.collect.ImmutableList;

import java.util.Optional;
import java.util.stream.Collectors;

public class GenerifiedType implements TypeReference {
	private final ClassReference actualType;

	private final ImmutableList<String> genericParameters;

	private GenerifiedType(ClassReference actualType, ImmutableList<String> genericParameters) {
		this.actualType = actualType;
		this.genericParameters = genericParameters;
	}

	@Override
	public String name() {
		return actualType.name() + genericParameters.stream().collect(Collectors.joining(",", "<", ">"));
	}

	@Override
	public Optional<String> importStatement() {
		return actualType.importStatement();
	}

	@Override
	public boolean nullable() {
		return actualType.nullable();
	}

	public static GenerifiedType of(String representation) {
		int start = representation.indexOf('<');
		int depth = 1;

		if (start == -1) {
			throw new IllegalArgumentException("String does not represent a generic type");
		}

		final String basename = representation.substring(0, start);
		ImmutableList.Builder<String> builder = ImmutableList.builder();

		String searchSpace = representation.substring(start + 1);
		StringBuilder current = new StringBuilder();

		for (int i = 0; i < searchSpace.length() && depth >= 1; i++) {
			char next = searchSpace.charAt(i);

			if (next == '<') {
				depth++;
			} else if (next == '>') {
				depth--;
			} else if (depth == 1 && next == ',') {
				builder.add(current.toString());
				current = new StringBuilder();
			} else {
				current.append(next);
			}
		}

		if (current.length() > 0) {
			builder.add(current.toString());
		}

		return new GenerifiedType(ClassReference.of(basename), builder.build());
	}
}
