package com.jeroensteenbeeke.andalite.java.transformation.returntypes;

import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;
import org.jetbrains.annotations.NotNull;

public enum FluentReturnType implements MethodReturnType {
	FLUENT {
		@Override
		@NotNull
		public String toJavaString(@NotNull ContainingDenomination<?, ?> containingDenomination) {
			return containingDenomination.getDenominationName();
		}
	}
}
