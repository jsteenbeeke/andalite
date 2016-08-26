package com.jeroensteenbeeke.andalite.java.transformation;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureConstructorOperation;

public class HasConstructorBuilder {
	private final Builder<ParameterDescriptor> descriptors;

	private final Consumer<IClassOperation> onCreate;

	HasConstructorBuilder(@Nonnull Consumer<IClassOperation> onCreate) {
		this.descriptors = ImmutableList.builder();
		this.onCreate = onCreate;
	}

	public ParameterDescriber withParameter(@Nonnull String name) {
		return new ParameterDescriber(this, name);
	}

	public EnsureConstructorOperation withAccessModifier(
			@Nonnull AccessModifier constructorModifier) {
		EnsureConstructorOperation ensureConstructorOperation = new EnsureConstructorOperation(
				constructorModifier, descriptors.build());
		onCreate.accept(ensureConstructorOperation);
		return ensureConstructorOperation;
	}

	public static class ParameterDescriber {
		private final HasConstructorBuilder builder;

		private final String name;

		private ParameterDescriber(HasConstructorBuilder builder, String name) {
			this.builder = builder;
			this.name = name;
		}

		public HasConstructorBuilder ofType(@Nonnull String type) {
			builder.descriptors.add(new ParameterDescriptor(type, name));
			return builder;
		}

	}
}
