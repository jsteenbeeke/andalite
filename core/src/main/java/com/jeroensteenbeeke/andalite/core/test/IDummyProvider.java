package com.jeroensteenbeeke.andalite.core.test;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * Interface for dummy file creation
 * 
 * @author Jeroen Steenbeeke
 *
 */
@FunctionalInterface
public interface IDummyProvider {
	/**
	 * Creates a new dummy file
	 * 
	 * @param tempFiles
	 *            A callback mechanism for registering temp files for cleanup
	 * @return A dummy file
	 * @throws IOException
	 *             If something goes wrong creating the dummy file
	 */
	@Nonnull
	File getDummy(@Nonnull ITempFileRegistry tempFiles) throws IOException;
}
