package com.jeroensteenbeeke.andalite.java.transformation;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEnumConstantOperation;

public class HasEnumConstantBuilder {
	private final Builder<String> descriptors;
	
	HasEnumConstantBuilder() {
		this.descriptors = ImmutableList.builder();
	}
	

	public HasEnumConstantBuilder withParameterExpression(@Nonnull String expression) {
		descriptors.add(expression);
		return this;
	}
	
	public HasEnumConstantBuilder withStringParameterExpression(@Nonnull String expression) {
		// FIXME: This probably does not cover the full range of expressions
		String escaped = String.format("\"%s\"", expression.replace("\"", "\\\""));
		
		descriptors.add(escaped);
		return this;
	}
	
	public IEnumOperation named(@Nonnull String name) {
		return new EnsureEnumConstantOperation(name, descriptors.build());
	}

}
