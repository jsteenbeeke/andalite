package com.jeroensteenbeeke.andalite.core;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.AfterClass;

import com.google.common.collect.Sets;

public abstract class TempFileCleaningTest {

	private static Set<File> createdTempFiles = Sets.newHashSet();

	protected static File newTempFile(String prefix, String suffix)
			throws IOException {
		File temp = File.createTempFile(prefix, suffix);
		createdTempFiles.add(temp);
		return temp;
	}

	protected Copyer copy(String pathName) {
		return new Copyer(pathName);
	}

	@AfterClass
	public static void removeTempFiles() {
		for (File file : createdTempFiles) {
			file.delete();
		}
	}

	public static class Copyer {
		private final String pathName;

		private Copyer(String pathName) {
			this.pathName = pathName;
		}

		public File as(String filename) throws IOException {
			File tempFile = newTempFile(filename, null);
			createdTempFiles.add(tempFile);
			return tempFile;
		}
	}
}
