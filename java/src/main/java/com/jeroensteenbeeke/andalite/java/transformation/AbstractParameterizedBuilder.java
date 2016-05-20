package com.jeroensteenbeeke.andalite.java.transformation;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;

public abstract class AbstractParameterizedBuilder<T, B extends AbstractParameterizedBuilder<T, B>> {
	final Builder<ParameterDescriptor> descriptors;

	private AccessModifier modifier;

	protected AbstractParameterizedBuilder(AccessModifier defaultAccess) {
		this.descriptors = ImmutableList.builder();

		this.modifier = defaultAccess;
	}

	@SuppressWarnings("unchecked")
	public ParameterDescriber<T, B> withParameter(@Nonnull String name) {
		return new ParameterDescriber<T, B>((B) this, name);
	}
	
	@SuppressWarnings("unchecked")
	public B withParameterOfType(@Nonnull String type) {
		descriptors.add(new ParameterDescriptor(type, null));
		return (B) this;
	}

	protected List<ParameterDescriptor> getDescriptors() {
		return descriptors.build();
	}

	protected AccessModifier getModifier() {
		return modifier;
	}

	@SuppressWarnings("unchecked")
	public B withModifier(@Nonnull AccessModifier modifier) {
		this.modifier = modifier;
		return (B) this;
	}

	public static class ParameterDescriber<T, B extends AbstractParameterizedBuilder<T, B>> {
		private final B builder;

		private final String name;

		private ParameterDescriber(B builder, String name) {
			this.builder = builder;
			this.name = name;
		}

		public B ofType(@Nonnull String type) {
			builder.descriptors.add(new ParameterDescriptor(type, name));
			return builder;
		}

	}

}
