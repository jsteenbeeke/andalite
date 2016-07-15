package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

public class ConstructorParameterAdditionBuilder {
	private final String identifier;

	public ConstructorParameterAdditionBuilder(String identifier) {
		this.identifier = identifier;
	}

	public AddConstructorParameterOperation ofType(String type) {
		return new AddConstructorParameterOperation(identifier, type);
	}
}
