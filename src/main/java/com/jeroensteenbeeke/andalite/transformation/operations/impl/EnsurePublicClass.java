package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.CompilationUnitOperation;

public class EnsurePublicClass implements CompilationUnitOperation {

	@Override
	public List<Transformation> perform(AnalyzedSourceFile input) {
		for (AnalyzedClass analyzedClass : input.getClasses()) {
			if (analyzedClass.getAccessModifier() == AccessModifier.PUBLIC) {
				return ImmutableList.of();
			}
		}

		return ImmutableList.of(Transformation.insertAfter(input, String
				.format("public class %s {\n\n}\n", stripExtension(input
						.getOriginalFile().getName()))));
	}

	private String stripExtension(@Nonnull String name) {
		StringBuilder builder = new StringBuilder();

		for (char c : name.toCharArray()) {
			if (c == '.') {
				break;
			}

			builder.append(c);
		}

		return builder.toString();
	}

}
