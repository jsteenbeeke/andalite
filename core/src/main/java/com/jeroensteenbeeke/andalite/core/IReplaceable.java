package com.jeroensteenbeeke.andalite.core;

public interface IReplaceable extends ILocatable {
	default Transformation replace(String replacement) {
		return Transformation.replace(this, replacement);
	}
}
