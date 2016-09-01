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
package com.jeroensteenbeeke.andalite.core.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.After;

import com.google.common.collect.Sets;

/**
 * Base class for unit tests, that provides temp files and automatically cleans
 * them.
 * 
 * @author Jeroen Steenbeeke
 *
 */
public abstract class DummyAwareTest implements ITempFileRegistry {
	/**
	 * A set of predefinied dummy classes for developer convenience. Dummy files
	 * are located in src/dummy/java/com/jeroensteenbeeke/andalite/dummy/
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	protected enum BaseDummies implements IDummyProvider {
		/**
		 * A bare Java class with no contents and no imports
		 */
		BareClass("BareClass"),
		/**
		 * A bare Java class with an unbounded generic type
		 */
		BareClassWithGenericType("BareClassWithGenericType"),
		/**
		 * A bare interface with no contents and no imports
		 */
		BareInterface("BareInterface"),
		/**
		 * An empty class file (only a package declaration)
		 */
		Empty("Empty"),
		/**
		 * A bare enum with no constants, contents or imports
		 */
		BareEnum("BareEnum"),
		/**
		 * A class implementing {@code Comparator}, comparing integers in
		 * reverse order
		 */
		ReverseIntComparator("ReverseIntComparator"),
		/**
		 * A class with methods containing if-statements
		 */
		IfStatements("IfStatements"),
		/**
		 * A class with enum constants that take Strings as parameters
		 */
		EnumWithStringParam("EnumWithStringParam"),
		/**
		 * A dummy descriptor that does not refer to a real dummy file
		 */
		OhMyGodThisIsNotARealClass("OhMyGodThisIsNotARealClass");

		private final String className;

		/**
		 * Creates a new dummy descriptor with the specified classname
		 * 
		 * @param className
		 *            The name of the class to use as dummy
		 */
		private BaseDummies(@Nonnull String className) {
			this.className = className;
		}

		@Override
		public File getDummy(ITempFileRegistry tempFiles) throws IOException {
			File tempFile = File.createTempFile(className, ".java");
			tempFiles.add(tempFile);

			String dummyLocation = "/com/jeroensteenbeeke/andalite/dummy/"
					.concat(String.format("%s.java", className));
			try (InputStream stream = DummyAwareTest.class
					.getResourceAsStream(dummyLocation);
					FileOutputStream fos = new FileOutputStream(tempFile)) {
				if (stream == null) {
					throw new IllegalArgumentException(String
							.format("Invalid dummy name %s.java", className));
				}

				int in;

				while ((in = stream.read()) != -1) {
					fos.write(in);
				}
				fos.flush();
			}

			return tempFile;
		}
	}

	/**
	 * Creates a new dummy file on-the-fly, based on the given file contents
	 * 
	 * @param body
	 *            The file contents
	 * @return A new {@code IDummyProvider} object representing the file
	 *         contents given
	 */
	@Nonnull
	protected IDummyProvider createProvider(@Nonnull StringBuilder body) {
		return createProvider(body.toString());
	}

	/**
	 * Creates a new dummy file on-the-fly, based on the given file contents
	 * 
	 * @param body
	 *            The file contents
	 * @return A new {@code IDummyProvider} object representing the file
	 *         contents given
	 */
	@Nonnull
	protected IDummyProvider createProvider(@Nonnull String body) {
		return (tempFiles) -> {
			File tempFile = File.createTempFile("on-the-fly", ".java");
			tempFiles.add(tempFile);

			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				String data = body.toString();

				fos.write(data.getBytes());

				fos.flush();
			}

			return tempFile;
		};
	}

	// Set of created files, used in cleanup
	private final Set<File> dummyFiles = Sets.newConcurrentHashSet();

	/**
	 * Returns a dummy file based on a given dummy descriptor
	 * 
	 * @param descriptor
	 * @return
	 * @throws IOException
	 */
	protected final File getDummy(IDummyProvider descriptor)
			throws IOException {
		return descriptor.getDummy(this);
	}

	@Override
	public void add(File tempFile) {
		dummyFiles.add(tempFile);
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
