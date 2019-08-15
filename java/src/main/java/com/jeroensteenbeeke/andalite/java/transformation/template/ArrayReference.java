package com.jeroensteenbeeke.andalite.java.transformation.template;

import java.util.Optional;

public class ArrayReference implements TypeReference {
	private final TypeReference arrayType;

	ArrayReference(TypeReference arrayType) {
		this.arrayType = arrayType;
	}

	@Override
	public String name() {
		return arrayType.name() + "[]";
	}

	@Override
	public Optional<String> importStatement() {
		return arrayType.importStatement();
	}

	@Override
	public boolean nullable() {
		return true;
	}
}
