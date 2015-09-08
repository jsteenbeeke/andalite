/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.testbase;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.AfterClass;

import com.google.common.collect.Sets;
import com.google.common.io.Files;

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
			Files.copy(new File(pathName), tempFile);
			return tempFile;
		}
	}
}
