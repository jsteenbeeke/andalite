package com.jeroensteenbeeke.andalite.java.transformation;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEnumConstantOperation;

public class HasEnumConstantBuilder {
	private final Builder<String> descriptors;

	private final Consumer<EnsureEnumConstantOperation> onCreate;

	HasEnumConstantBuilder(
			@Nonnull Consumer<EnsureEnumConstantOperation> onCreate) {
		this.descriptors = ImmutableList.builder();
		this.onCreate = onCreate;
	}

	public HasEnumConstantBuilder withParameterExpression(
			@Nonnull String expression) {
		descriptors.add(expression);
		return this;
	}

	public HasEnumConstantBuilder withStringParameterExpression(
			@Nonnull String expression) {
		// FIXME: This probably does not cover the full range of expressions
		String escaped = String.format("\"%s\"",
				expression.replace("\"", "\\\""));

		descriptors.add(escaped);
		return this;
	}

	public IEnumOperation named(@Nonnull String name) {
		EnsureEnumConstantOperation enumConstantOperation = new EnsureEnumConstantOperation(
				name, descriptors.build());
		onCreate.accept(enumConstantOperation);
		return enumConstantOperation;
	}

}
