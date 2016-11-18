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

import javax.annotation.Nonnull;

/**
 * Utility class for locating files in a Maven project-like structure
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class FileLocator {
	private static final String SRC = "src";

	private static final String MAIN = "main";

	private static final String TEST = "test";

	private static final String JAVA = "java";

	private static final String RESOURCES = "resources";

	private static final String WEBAPP = "webapp";

	private FileLocator() {
	}

	/**
	 * Attempt to locate a file relative to the given root
	 * 
	 * @param root
	 *            The root to use as base
	 * @return An {@code InRootLocator} object
	 */
	@Nonnull
	public static InRootLocator relativeTo(@Nonnull File root) {
		return new InRootLocator(root);
	}

	/**
	 * Attempt to locate a file relative to the given Maven module
	 * 
	 * @param moduleName
	 *            The name of the module to search in
	 * @return An {@code InModuleLocatorInitializer} object
	 */
	@Nonnull
	public static InModuleLocatorInitializer inModule(
			@Nonnull String moduleName) {
		return new InModuleLocatorInitializer(moduleName);
	}

	/**
	 * Module-specific file locator builder step. Used to specify the base
	 * folder to resolve the path to
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	public static class InModuleLocatorInitializer {
		private final String moduleName;

		private InModuleLocatorInitializer(@Nonnull String moduleName) {
			this.moduleName = moduleName;
		}

		/**
		 * Set the builder to resolve the module-specific path relative to the
		 * given root
		 * 
		 * @param root
		 *            The root to base the path off of
		 * @return An {@code InModuleLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InModuleLocator relativeTo(@Nonnull File root)
				throws FileLocatorException {
			File module = new File(root, moduleName);
			ensureDirectoryExists(module);

			return new InModuleLocator(module);
		}
	}

	/**
	 * Module-specific file locator builder step. Used to determine the
	 * Maven-specific folder
	 * to resolve files in
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	public static class InModuleLocator {

		private final File moduleFolder;

		private InModuleLocator(@Nonnull File moduleFolder) {
			super();
			this.moduleFolder = moduleFolder;
		}

		/**
		 * Locate the file within the {@code src/main/java} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InPackageLocator mainSources() throws FileLocatorException {
			return new InPackageLocator(new File(
					new File(new File(moduleFolder, SRC), MAIN), JAVA));
		}

		/**
		 * Locate the file within the {@code src/test/java} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 * 
		 */
		@Nonnull
		public InPackageLocator testSources() throws FileLocatorException {
			return new InPackageLocator(new File(
					new File(new File(moduleFolder, SRC), TEST), JAVA));
		}

		/**
		 * Locate the file within the {@code src/main/resources} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InPackageLocator mainResources() throws FileLocatorException {
			return new InPackageLocator(new File(
					new File(new File(moduleFolder, SRC), MAIN), RESOURCES));
		}

		/**
		 * Locate the file within the {@code src/test/resources} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 * 
		 */
		@Nonnull
		public InPackageLocator testResources() throws FileLocatorException {
			return new InPackageLocator(new File(
					new File(new File(moduleFolder, SRC), TEST), RESOURCES));
		}

		/**
		 * Locate the file within the {@code src/main/webapp} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InPackageLocator webappSources() throws FileLocatorException {
			return new InPackageLocator(new File(
					new File(new File(moduleFolder, SRC), MAIN), WEBAPP));
		}
	}

	/**
	 * Root-relative file locator builder
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	public static class InRootLocator {
		private final File root;

		private InRootLocator(File root) {
			this.root = new File(root, SRC);
		}

		/**
		 * Locate the file within the {@code src/main/java} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InPackageLocator mainSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, MAIN), JAVA));
		}

		/**
		 * Locate the file within the {@code src/test/java} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 * 
		 */
		@Nonnull
		public InPackageLocator testSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, TEST), JAVA));
		}

		/**
		 * Locate the file within the {@code src/main/resources} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InPackageLocator mainResources() throws FileLocatorException {
			return new InPackageLocator(
					new File(new File(root, MAIN), RESOURCES));
		}

		/**
		 * Locate the file within the {@code src/test/resources} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 * 
		 */
		@Nonnull
		public InPackageLocator testResources() throws FileLocatorException {
			return new InPackageLocator(
					new File(new File(root, TEST), RESOURCES));
		}

		/**
		 * Locate the file within the {@code src/main/webapp} directory
		 * 
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InPackageLocator webappSources() throws FileLocatorException {
			return new InPackageLocator(new File(new File(root, MAIN), WEBAPP));
		}
	}

	/**
	 * Resolver class for locating package-specific files within a Maven project
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	public static class InPackageLocator {
		private final File contextFolder;

		private InPackageLocator(@Nonnull File contextFolder)
				throws FileLocatorException {
			this.contextFolder = contextFolder;
			ensureDirectoryExists(contextFolder);
		}

		/**
		 * Resolve the file relative to the given Java package
		 * 
		 * @param packageName
		 *            The package name (dot-separated)
		 * @return An {@code InPackageLocator} object
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public InPackageLocator inPackage(@Nonnull String packageName)
				throws FileLocatorException {
			String[] packages = packageName.split("\\.");
			File next = contextFolder;
			for (String p : packages) {
				next = new File(next, p);
				ensureDirectoryExists(next);
			}

			return new InPackageLocator(next);
		}

		/**
		 * Get the file with the given filename, relative to the path previously
		 * specified
		 * 
		 * @param filename
		 *            The name of the file to locate
		 * @return The given File. May not yet exist, check with
		 *         {@code File.exists}
		 */
		@Nonnull
		public File getFile(@Nonnull String filename) {
			return new File(contextFolder, filename);
		}

		/**
		 * Locate a java class based on the given fully qualified domain name.
		 * 
		 * @param fqdn
		 *            The fully qualified domain name of the class to locate
		 * @return The given File. May not yet exist, check with
		 *         {@code File.exists}
		 * @throws FileLocatorException
		 *             If the given path is invalid, i.e. it is not a directory,
		 *             or it does not exist and cannot be created
		 */
		@Nonnull
		public File getClass(@Nonnull String fqdn) throws FileLocatorException {
			String[] parts = fqdn.split("\\.");
			File next = contextFolder;
			for (String p : parts) {
				if (p.equals(parts[parts.length - 1])) {
					next = new File(next, p.concat(".java"));
					if (!next.exists() || next.isDirectory()) {
						throw new FileLocatorException(
								"File %s does not exist or is a directory",
								next.getAbsolutePath());
					}
				} else {
					next = new File(next, p);
					ensureDirectoryExists(next);
				}
			}

			return next;
		}

	}

	/**
	 * Checks if the given file exists. If it does, checks if it is a directory.
	 * If it does not, attempts to create it
	 * 
	 * @param desiredDirectory
	 *            The file to check
	 * @throws FileLocatorException
	 *             If the file exists and is not a directory, OR if the file
	 *             does not exist and cannot be created
	 */
	private static void ensureDirectoryExists(@Nonnull File desiredDirectory)
			throws FileLocatorException {
		if (!desiredDirectory.exists()) {
			if (!desiredDirectory.mkdir()) {
				throw new FileLocatorException(
						"Directory %s does not exist and cannot be created",
						desiredDirectory.getAbsolutePath());
			}
		}
		if (!desiredDirectory.isDirectory()) {
			throw new FileLocatorException("File %s is not a directory",
					desiredDirectory.getAbsolutePath());
		}
	}
}
