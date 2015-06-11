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
package com.jeroensteenbeeke.andalite.forge.util;

import java.io.File;

public class FileLocator {

	private FileLocator() {
	}

	public static InModuleLocator inModule(String moduleName)
			throws FileLocatorException {
		File module = new File(moduleName);
		checkFile(module);

		return new InModuleLocator(module);
	}

	public static class InModuleLocator {
		private static final String SRC = "src";

		private static final String MAIN = "main";

		private static final String TEST = "test";

		private static final String JAVA = "java";

		private static final String RESOURCES = "resources";

		private static final String WEBAPP = "webapp";

		private final File moduleFolder;

		private InModuleLocator(File moduleFolder) {
			super();
			this.moduleFolder = moduleFolder;
		}

		public InPackageLocator mainSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(
					moduleFolder, SRC), MAIN), JAVA));
		}

		public InPackageLocator testSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(
					moduleFolder, SRC), TEST), JAVA));
		}

		public InPackageLocator mainResources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(
					moduleFolder, SRC), MAIN), RESOURCES));
		}

		public InPackageLocator testResources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(
					moduleFolder, SRC), TEST), RESOURCES));
		}

		public InPackageLocator webappSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(
					moduleFolder, SRC), MAIN), WEBAPP));
		}
	}

	public static class InPackageLocator {
		private final File contextFolder;

		private InPackageLocator(File contextFolder)
				throws FileLocatorException {
			this.contextFolder = contextFolder;
			checkFile(contextFolder);
		}

		public InPackageLocator inPackage(String packageName)
				throws FileLocatorException {
			String[] packages = packageName.split("\\.");
			File next = contextFolder;
			for (String p : packages) {
				next = new File(next, p);
				checkFile(next);
			}

			return new InPackageLocator(next);
		}

		public File getFile(String filename) {
			return new File(contextFolder, filename);
		}

	}

	private static void checkFile(File next) throws FileLocatorException {
		if (!next.exists()) {
			throw new FileLocatorException("File %s does not exist",
					next.getAbsolutePath());
		}
		if (!next.isDirectory()) {
			throw new FileLocatorException("File %s is not a directory",
					next.getAbsolutePath());
		}
	}
}
