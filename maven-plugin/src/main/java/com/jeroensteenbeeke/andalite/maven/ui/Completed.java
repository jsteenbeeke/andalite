package com.jeroensteenbeeke.andalite.maven.ui;

import com.jeroensteenbeeke.andalite.forge.ui.Action;

public class Completed implements Action {
	private static final Completed instance = new Completed();

	private Completed() {

	}

	public static Completed get() {
		return instance;
	}
}
