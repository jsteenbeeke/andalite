package com.jeroensteenbeeke.andalite.core;

import java.io.File;

import javax.annotation.Nonnull;

/**
 * Context class for storing execution-environment related information, such as
 * the working directory for transformation operations
 * 
 * @author Jeroen Steenbeeke
 */
public final class AndaliteContext {
	private static File workingDirectory = initWorkingDirectory();

	/**
	 * Private constructor to prevent creating instances of this class
	 */
	private AndaliteContext() {

	}

	/**
	 * Initializes the working directory to {@code System.getProperty("user.dir")}
	 * @return A File object pointing to {@code System.getProperty("user.dir")}
	 */
	private static File initWorkingDirectory() {
		return new File(System.getProperty("user.dir"));
	}

	/**
	 * Get the current working directory for Andalite-based operaations
	 * 
	 * @return A {@code File} pointing to the current working directory.
	 *         Defaults to {@code System.getProperty("user.dir")}, but can be
	 *         overridden
	 */
	@Nonnull
	public static File getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * Sets the working directory to the indicated file
	 * @param file The file to use as a working directory
	 */
	public static void setWorkingDirectory(@Nonnull File file) {
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("Working directory should be a file!");
		}
		workingDirectory = file;
	}
}
