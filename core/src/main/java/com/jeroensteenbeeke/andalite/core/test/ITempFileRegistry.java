package com.jeroensteenbeeke.andalite.core.test;

import java.io.File;

/**
 * Registry of created tempfiles for unit tests. Used in cleanup
 * 
 * @author Jeroen Steenbeeke
 *
 */
public interface ITempFileRegistry {
	/**
	 * Add a new file to the registry
	 * 
	 * @param tempFile
	 *            The temporary file to delete after completion
	 */
	void add(File tempFile);
}
