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

	public static InRootLocator relativeTo(File root) {
		return new InRootLocator(root);
	}

	public static InModuleLocatorInitializer inModule(String moduleName) {
		return new InModuleLocatorInitializer(moduleName);
	}

	public static class InModuleLocatorInitializer {
		private final String moduleName;

		public InModuleLocatorInitializer(String moduleName) {
			this.moduleName = moduleName;
		}

		public InModuleLocator relativeTo(File root) throws FileLocatorException {
			File module = new File(root, moduleName);
			checkFile(module);

			return new InModuleLocator(module);
		}
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
			return new InPackageLocator(new File(new File(new File(moduleFolder, SRC), MAIN), JAVA));
		}

		public InPackageLocator testSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(moduleFolder, SRC), TEST), JAVA));
		}

		public InPackageLocator mainResources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(moduleFolder, SRC), MAIN), RESOURCES));
		}

		public InPackageLocator testResources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(moduleFolder, SRC), TEST), RESOURCES));
		}

		public InPackageLocator webappSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(new File(moduleFolder, SRC), MAIN), WEBAPP));
		}
	}

	public static class InRootLocator {
		private static final String SRC = "src";

		private static final String MAIN = "main";

		private static final String TEST = "test";

		private static final String JAVA = "java";

		private static final String RESOURCES = "resources";

		private static final String WEBAPP = "webapp";

		private final File root;

		private InRootLocator(File root) {
			this.root = new File(root, SRC);
		}

		public InPackageLocator mainSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, MAIN), JAVA));
		}

		public InPackageLocator testSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, TEST), JAVA));
		}

		public InPackageLocator mainResources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, MAIN), RESOURCES));
		}

		public InPackageLocator testResources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, TEST), RESOURCES));
		}

		public InPackageLocator webappSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, MAIN), WEBAPP));
		}
	}

	public static class InPackageLocator {
		private final File contextFolder;

		private InPackageLocator(File contextFolder) throws FileLocatorException {
			this.contextFolder = contextFolder;
			checkFile(contextFolder);
		}

		public InPackageLocator inPackage(String packageName) throws FileLocatorException {
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

		public File getClass(String fqdn) throws FileLocatorException {
			String[] parts = fqdn.split("\\.");
			File next = contextFolder;
			for (String p : parts) {
				String filename = p.equals(parts[parts.length - 1]) ? p.concat(".java") : p;

				next = new File(next, filename);
				checkFile(next);
			}

			return next;
		}

	}

	private static void checkFile(File next) throws FileLocatorException {
		if (!next.exists()) {
			if (!next.mkdir()) {
				throw new FileLocatorException("Directory %s does not exist and cannot be created",
						next.getAbsolutePath());
			}
		}
		if (!next.isDirectory()) {
			throw new FileLocatorException("File %s is not a directory", next.getAbsolutePath());
		}
	}
}
