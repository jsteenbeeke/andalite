package com.jeroensteenbeeke.andalite.forge;

import java.io.File;

public final class ForgeContext {
	private static File workingDirectory = initWorkingDirectory();

	private ForgeContext() {

	}

	private static File initWorkingDirectory() {
		return new File(System.getProperty("user.dir"));
	}

	public static File getWorkingDirectory() {
		return workingDirectory;
	}

	public static void setWorkingDirectory(File file) {
		if (!file.isDirectory()) {
			throw new IllegalArgumentException(
					"Working directory should be a file!");
		}
		workingDirectory = file;
	}
}