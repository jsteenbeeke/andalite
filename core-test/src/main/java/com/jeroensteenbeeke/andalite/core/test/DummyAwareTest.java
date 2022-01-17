/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.core.test;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class DummyAwareTest implements ITempFileRegister {
	protected enum BaseDummies implements IDummyDescriptor {
		BareClass("BareClass"), BareClassWithGenericType(
			"BareClassWithGenericType"), BareInterface("BareInterface"), Empty(
			"Empty"), BareEnum("BareEnum"), BarestEnum("BarestEnum"), ReverseIntComparator(
			"ReverseIntComparator"), IfStatements("IfStatements"), EnumWithStringParam(
			"EnumWithStringParam"), OhMyGodThisIsNotARealClass(
			"OhMyGodThisIsNotARealClass"), EnumWithValuesPresent("EnumWithValuesPresent"), EnumWithValuesPresentNoSemi("EnumWithValuesPresentNoSemi");

		private final String className;

		BaseDummies(@NotNull String className) {
			this.className = className;
		}

		@Override
		@NotNull
		public File getDummy(@NotNull ITempFileRegister tempFiles) throws IOException {
			File tempFile = File.createTempFile(className, ".java");
			tempFiles.add(tempFile);

			try (InputStream stream = DummyAwareTest.class
				.getResourceAsStream("/com/jeroensteenbeeke/andalite/dummy/"
										 .concat(String.format("%s.java", className)));
				 FileOutputStream fos = new FileOutputStream(tempFile)) {
				if (stream == null) {
					throw new IllegalArgumentException(String.format(
						"Invalid class name %s", className));
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

	protected IDummyDescriptor createDescriptor(@NotNull StringBuilder body) {
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

	private final Set<File> dummyFiles = new ConcurrentSkipListSet<>();

	protected final File getDummy(@NotNull IDummyDescriptor descriptor)
		throws IOException {
		return descriptor.getDummy(this);
	}

	@Override
	public void add(@NotNull File tempFile) {
		dummyFiles.add(tempFile);
	}

	@AfterEach
	public void cleanUpDummies() {
		if (System.getProperty("dummies.retain") == null) {
			if (!dummyFiles.isEmpty()) {
				for (File file : dummyFiles) {
					assertTrue(file.delete());
				}
			}
		}
	}
}
