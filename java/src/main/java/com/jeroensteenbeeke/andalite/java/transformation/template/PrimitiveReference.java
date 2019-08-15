package com.jeroensteenbeeke.andalite.java.transformation.template;

import java.util.Optional;

public class PrimitiveReference implements TypeReference {
	private final String name;

	PrimitiveReference(String name) {
		this.name = name;
	}


	@Override
	public String name() {
		return name;
	}

	@Override
	public Optional<String> importStatement() {
		return Optional.empty();
	}

	@Override
	public boolean nullable() {
		return false;
	}
}
