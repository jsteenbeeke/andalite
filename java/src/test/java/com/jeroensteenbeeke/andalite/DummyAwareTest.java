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
package com.jeroensteenbeeke.andalite;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.After;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;

public abstract class DummyAwareTest {
	private final Set<File> dummyFiles = Sets.newConcurrentHashSet();

	protected final File getDummy(String className) throws IOException {
		File original = new File(
				"src/dummy/java/com/jeroensteenbeeke/andalite/dummy/",
				String.format("%s.java", className));

		File tempFile = File.createTempFile(className, ".java");

		Files.copy(original, tempFile);

		dummyFiles.add(tempFile);

		return tempFile;
	}

	protected final ClassAnalyzer analyzeDummy(String className)
			throws IOException {
		return new ClassAnalyzer(getDummy(className));
	}

	@After
	public void cleanUpDummies() {
		if (System.getProperty("dummies.retain") == null) {
			if (!dummyFiles.isEmpty()) {
				for (File file : dummyFiles) {
					file.delete();
				}
			}
		}
	}
}
