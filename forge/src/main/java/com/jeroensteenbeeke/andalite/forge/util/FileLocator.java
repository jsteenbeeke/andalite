package com.jeroensteenbeeke.andalite.forge.util;

import java.io.File;

public class FileLocator {

	private FileLocator() {
	}

	public InModuleLocator inModule(String moduleName) {
		File module = new File(moduleName);
		if (!module.exists()) {
			throw new IllegalArgumentException(String.format(
					"Module %s does not exist", moduleName));
		}
		if (!module.isDirectory()) {
			throw new IllegalArgumentException(String.format(
					"Module %s is not a directory", moduleName));
		}

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

		public PackageLocator mainSources() {
			return new PackageLocator(new File(new File(new File(moduleFolder,
					SRC), MAIN), JAVA));
		}

		public PackageLocator testSources() {
			return new PackageLocator(new File(new File(new File(moduleFolder,
					SRC), TEST), JAVA));
		}

		public PackageLocator mainResources() {
			return new PackageLocator(new File(new File(new File(moduleFolder,
					SRC), MAIN), RESOURCES));
		}

		public PackageLocator testResources() {
			return new PackageLocator(new File(new File(new File(moduleFolder,
					SRC), TEST), RESOURCES));
		}

		public InPackageLocator webappSources() {
			return new InPackageLocator(new File(new File(new File(
					moduleFolder, SRC), MAIN), WEBAPP));
		}
	}

	public static class PackageLocator {
		private final File contextFolder;

		private PackageLocator(File contextFolder) {
			this.contextFolder = contextFolder;
		}

	}

	public static class InPackageLocator {
		private final File contextFolder;

		private InPackageLocator(File contextFolder) {
			this.contextFolder = contextFolder;
		}

	}

}
