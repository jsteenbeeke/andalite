package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

public class ConstructorParameterAdditionBuilder {
	private final String identifier;

	private final Consumer<AddConstructorParameterOperation> onCreate;

	public ConstructorParameterAdditionBuilder(@NotNull String identifier,
			@NotNull Consumer<AddConstructorParameterOperation> onCreate) {
		this.identifier = identifier;
		this.onCreate = onCreate;
	}

	@NotNull
	public AddConstructorParameterOperation ofType(@NotNull String type) {
		AddConstructorParameterOperation operation = new AddConstructorParameterOperation(
				identifier, type);
		onCreate.accept(operation);
		return operation;
	}
}
