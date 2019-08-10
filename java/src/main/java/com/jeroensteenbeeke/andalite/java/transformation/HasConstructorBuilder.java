package com.jeroensteenbeeke.andalite.java.transformation;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IOutputable;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.ConstructableDenomination;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureConstructorOperation;

public class HasConstructorBuilder<O extends EnsureConstructorOperation<T,I>, T extends ConstructableDenomination<T,I>,I extends Enum<I> & IInsertionPoint<T>> {
	private final Builder<ParameterDescriptor> descriptors;

	private final Consumer<O> onCreate;

	private final BiFunction<AccessModifier, List<ParameterDescriptor>, O> finalizer;

	HasConstructorBuilder(@Nonnull Consumer<O> onCreate, BiFunction<AccessModifier, List<ParameterDescriptor>, O> finalizer) {
		this.descriptors = ImmutableList.builder();
		this.onCreate = onCreate;
		this.finalizer = finalizer;
	}

	public ParameterDescriber withParameter(@Nonnull String name) {
		return new ParameterDescriber(this, name);
	}

	public EnsureConstructorOperation<T,I> withAccessModifier(
			@Nonnull AccessModifier constructorModifier) {
		O ensureConstructorOperation = finalizer.apply(
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
