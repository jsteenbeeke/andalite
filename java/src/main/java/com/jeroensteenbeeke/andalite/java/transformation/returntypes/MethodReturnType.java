package com.jeroensteenbeeke.andalite.java.transformation.returntypes;

import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;
import org.jetbrains.annotations.NotNull;

public sealed interface MethodReturnType permits VoidReturnType, NamedReturnType, FluentReturnType {
	@NotNull
	String toJavaString(@NotNull ContainingDenomination<?, ?> containingDenomination);
}
