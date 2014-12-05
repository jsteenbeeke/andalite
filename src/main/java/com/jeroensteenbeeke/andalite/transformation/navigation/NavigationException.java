package com.jeroensteenbeeke.andalite.transformation.navigation;

import javax.annotation.Nonnull;

public class NavigationException extends Exception {

	private static final long serialVersionUID = 1L;

	public NavigationException(@Nonnull String message,
			@Nonnull Object... params) {
		super(params.length > 0 ? String.format(message, params) : message);
	}

}
