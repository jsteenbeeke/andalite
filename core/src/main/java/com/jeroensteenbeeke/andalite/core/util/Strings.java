package com.jeroensteenbeeke.andalite.core.util;

/**
 * Utility class for String-manipulation
 * 
 * @author Jeroen Steenbeeke
 */
public class Strings {
	/**
	 * Method to conditionally use {@code String.format} if the params argument
	 * is non-empty
	 * 
	 * @param format
	 *            The format String, or regular String if params is empty
	 * @param params
	 *            An array of arguments to pass to {@code String.format}
	 * @return The formatted String, or the input String if params is empty
	 */
	public static String conditionalFormat(String format, Object[] params) {
		if (params.length == 0) {
			return format;
		}

		return String.format(format, params);
	}

}
