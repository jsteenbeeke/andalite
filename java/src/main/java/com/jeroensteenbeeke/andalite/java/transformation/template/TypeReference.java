package com.jeroensteenbeeke.andalite.java.transformation.template;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface TypeReference {
	String name();

	Optional<String> importStatement();

	boolean nullable();

	static TypeReference of(@Nonnull String representation) {
		if (representation.endsWith("[]")) {
			return new ArrayReference(TypeReference.of(representation.substring(0, representation.length()-2)));
		}

		if (representation.contains("<")) {
			return GenerifiedType.of(representation);
		}

		switch (representation) {
			case "String":
				return ClassReference.of("java.lang.String");
			case "void":
			case "boolean":
			case "byte":
			case "short":
			case "int":
			case "long":
			case "float":
			case "double":
				return new PrimitiveReference(representation);
			default:
				return ClassReference.of(representation);
		}
	}
}
