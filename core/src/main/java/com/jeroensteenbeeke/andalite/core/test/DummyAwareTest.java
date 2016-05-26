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

import org.junit.After;

import com.google.common.collect.Sets;

public abstract class DummyAwareTest implements ITempFileRegister {
	protected enum BaseDummies implements IDummyDescriptor {
		BareClass("BareClass"), BareClassWithGenericType(
				"BareClassWithGenericType"), BareInterface("BareInterface"), Empty(
				"Empty"), BareEnum("BareEnum"), ReverseIntComparator(
				"ReverseIntComparator"), IfStatements("IfStatements"), EnumWithStringParam(
				"EnumWithStringParam"), OhMyGodThisIsNotARealClass(
				"OhMyGodThisIsNotARealClass");

		private final String className;

		private BaseDummies(String className) {
			this.className = className;
		}

		@Override
		public File getDummy(ITempFileRegister tempFiles) throws IOException {
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

	protected IDummyDescriptor createDescriptor(StringBuilder body) {
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

	private final Set<File> dummyFiles = Sets.newConcurrentHashSet();

	protected final File getDummy(IDummyDescriptor descriptor)
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
