package com.jeroensteenbeeke.andalite.java.analyzer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of a thrown exception
 * 
 * @author Jeroen Steenbeeke
 */
public class AnalyzedThrownException extends Locatable {
	private final String exception;

	/**
	 * Create a new AnalyzedThrownException
	 * 
	 * @param location
	 *            The location of
	 * @param exception
	 *            The exception that is thrown
	 */
	AnalyzedThrownException(@Nonnull Location location,
			@Nonnull String exception) {
		super(location);
		this.exception = exception;
	}

	/**
	 * Get the exception thrown
	 * 
	 * @return The exception thrown
	 */
	@Nonnull
	public String getException() {
		return exception;
	}

	@Override
	public void output(IOutputCallback callback) {
		callback.write(exception);
	}
}
