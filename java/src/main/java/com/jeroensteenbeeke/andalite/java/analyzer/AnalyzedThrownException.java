package com.jeroensteenbeeke.andalite.java.analyzer;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;

public class AnalyzedThrownException extends Locatable {
	private final String exception;

	public AnalyzedThrownException(Location location, String exception) {
		super(location);
		this.exception = exception;
	}

	public String getException() {
		return exception;
	}

	@Override
	public void output(IOutputCallback callback) {
		callback.write(exception);
	}
}
