package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

public class ConstructorParameterAdditionBuilder {
	private final String identifier;

	private final Consumer<AddConstructorParameterOperation> onCreate;

	public ConstructorParameterAdditionBuilder(@Nonnull String identifier,
			@Nonnull Consumer<AddConstructorParameterOperation> onCreate) {
		this.identifier = identifier;
		this.onCreate = onCreate;
	}

	@Nonnull
	public AddConstructorParameterOperation ofType(@Nonnull String type) {
		AddConstructorParameterOperation operation = new AddConstructorParameterOperation(
				identifier, type);
		onCreate.accept(operation);
		return operation;
	}
}
