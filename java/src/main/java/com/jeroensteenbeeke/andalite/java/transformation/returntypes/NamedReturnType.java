package com.jeroensteenbeeke.andalite.java.transformation.returntypes;

import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;
import org.jetbrains.annotations.NotNull;

public record NamedReturnType(@NotNull String name) implements MethodReturnType {
	@Override
	@NotNull
	public String toJavaString(@NotNull ContainingDenomination<?, ?> containingDenomination) {
		return name;
	}
}
