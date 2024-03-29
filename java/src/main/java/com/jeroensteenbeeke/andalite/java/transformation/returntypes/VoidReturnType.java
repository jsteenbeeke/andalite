package com.jeroensteenbeeke.andalite.java.transformation.returntypes;

import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;
import org.jetbrains.annotations.NotNull;

public enum VoidReturnType implements MethodReturnType {
	VOID {
		@Override
		@NotNull
		public String toJavaString(@NotNull ContainingDenomination<?, ?> containingDenomination) {
			return "void";
		}
	}
}
