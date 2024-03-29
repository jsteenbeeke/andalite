package com.jeroensteenbeeke.andalite.java.transformation.template;

import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.NamedReturnType;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.VoidReturnType;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public interface TypeReference {
	String name();

	Optional<String> importStatement();

	boolean nullable();

	static TypeReference of(@NotNull String representation) {
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
	@NotNull
	default MethodReturnType toMethodReturnType() {
		if ("void".equals(name())) {
			return VoidReturnType.VOID;
		} else {
			return new NamedReturnType(name());
		}
	}
}
