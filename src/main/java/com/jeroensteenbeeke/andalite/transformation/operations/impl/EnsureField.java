package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public class EnsureField implements ClassOperation {
	private final String name;

	private final String type;

	private final AccessModifier modifier;

	public EnsureField(@Nonnull String name, @Nonnull String type,
			@Nonnull AccessModifier modifier) {
		super();
		this.name = name;
		this.type = type;
		this.modifier = modifier;
	}

	@Override
	public List<Transformation> perform(@Nonnull AnalyzedClass input)
			throws OperationException {
		if (input.hasField(name)) {
			AnalyzedField field = input.getField(name);

			if (!type.equals(field.getType())) {
				throw new OperationException(String.format(
						"Field %s should have type %s but instead has type %s",
						name, type, field.getType()));
			}

			if (!modifier.equals(field.getAccessModifier())) {
				throw new OperationException(
						String.format(
								"Field %s should have access modifier %s but instead has access modifier %s",
								name, modifier, field.getAccessModifier()));
			}
		}

		return ImmutableList.of(Transformation.insertBefore(input
				.getBodyLocation(), String.format("\n%s%s%s;\n\n",
				modifier.getOutput(), type, name)));
	}

}
