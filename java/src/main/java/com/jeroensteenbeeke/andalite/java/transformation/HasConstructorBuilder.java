package com.jeroensteenbeeke.andalite.java.transformation;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureConstructorOperation;

public class HasConstructorBuilder {
	private final Builder<ParameterDescriptor> descriptors;
	
	HasConstructorBuilder() {
		this.descriptors = ImmutableList.builder();
	}
	

	public ParameterDescriber withParameter(@Nonnull String name) {
		return new ParameterDescriber(this, name);
	}
	
	public IClassOperation withAccessModifier(@Nonnull AccessModifier constructorModifier) {
		return new EnsureConstructorOperation(constructorModifier, descriptors.build());
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
