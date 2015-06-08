package com.jeroensteenbeeke.andalite.forge.util;

public class FileLocatorException extends Exception {
	private static final long serialVersionUID = 1L;

	public FileLocatorException(String message, Object... params) {
		super(String.format(message, params));
	}

}
