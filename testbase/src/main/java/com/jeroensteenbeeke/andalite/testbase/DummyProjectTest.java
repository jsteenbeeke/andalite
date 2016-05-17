package com.jeroensteenbeeke.andalite.testbase;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.common.util.concurrent.Atomics;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;

public abstract class DummyProjectTest {
	private static final Logger log = LoggerFactory
			.getLogger(DummyProjectTest.class);

	public static final String KEY_SOURCE_PATH = "sourcePath";

	private static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private static final String SRC_MAIN_JAVA = String.format(
			"src%smain%sjava", FILE_SEPARATOR, FILE_SEPARATOR);

	private static final String SRC_TEST_JAVA = String.format(
			"src%stest%sjava", FILE_SEPARATOR, FILE_SEPARATOR);

	/**
	 * Imports that, if used without a class or wildcard added, are invalid
	 */
	private static String[] DISALLOWED_IMPORTS = { "java.lang", "java.util",
			"java.math" };

	private final DummyProjectContext context;

	private File baseFolder;

	protected DummyProjectTest(DummyProjectContext context) {
		this.context = context;
	}

	private static String moduleLocation(String moduleName, String sourceName) {
		return String.format("%s%s%s", moduleName, FILE_SEPARATOR, sourceName);
	}

	protected void onCreateConfig(Map<String, String> config) {

	}

	protected final Map<String, String> createConfig() {
		Map<String, String> config = Maps.newHashMap();

		config.put(KEY_SOURCE_PATH,
				new File(baseFolder, SRC_MAIN_JAVA).getAbsolutePath());
		onCreateConfig(config);

		return ImmutableMap.copyOf(config);
	}

	@Before
	public void initContext() {
		Multimap<String, DummyFile> packageDummies = LinkedHashMultimap
				.create();
		context.getDummyFilesToCreate().forEach(d -> {
			packageDummies.put(asFolder(d.getPackageName()), d);
		});

		AtomicBoolean success = new AtomicBoolean(true);
		AtomicReference<String> result = Atomics.newReference("");

		baseFolder = Files.createTempDir();

		context.getFoldersToCreate()
				.forEach(
						c -> {
							if (!success.get()) {
								return;
							}

							File f = new File(baseFolder, c);
							if (c.contains(FILE_SEPARATOR)) {
								if (!f.exists() && !f.mkdirs()) {
									success.set(false);
									result.set(String.format(
											"Could not create directory %s",
											f.getAbsolutePath()));
								} else {
									log.info("Created folder {}",
											f.getAbsolutePath());
								}
							} else {
								if (!f.exists() && !f.mkdir()) {
									success.set(false);
									result.set(String.format(
											"Could not create directory %s",
											f.getAbsolutePath()));
								} else {
									log.info("Created folder {}",
											f.getAbsolutePath());
								}
							}

							if (packageDummies.containsKey(c)) {
								packageDummies
										.get(c)
										.forEach(
												d -> {
													File dummy = new File(f, d
															.getName());
													try (FileOutputStream fos = new FileOutputStream(
															dummy)) {
														for (char e : d
																.getSource()
																.toCharArray()) {
															fos.write(e);
														}
														fos.flush();
														log.info(
																"Created file {}",
																dummy.getAbsolutePath());
													} catch (IOException ioe) {
														result.set(ioe
																.getMessage());
														success.set(false);
													}
												});
							}
						});

		assertTrue(result.get(), success.get());
	}

	protected final File getBaseFolder() {
		return baseFolder;
	}

	public ActionResult validateModuleMainJavaClass(String module, String fqdn,
			String... disallowedPackages) {
		return validateJavaClass(moduleLocation(module, SRC_MAIN_JAVA), fqdn,
				disallowedPackages);
	}

	public ActionResult validateMainJavaClass(String fqdn,
			String... disallowedPackages) {
		return validateJavaClass(SRC_MAIN_JAVA, fqdn, disallowedPackages);
	}

	public ActionResult validateTestJavaClass(String fqdn,
			String... disallowedPackages) {
		return validateJavaClass(SRC_TEST_JAVA, fqdn, disallowedPackages);
	}

	private ActionResult validateJavaClass(String source, String fqdn,
			String... disallowedPackages) {
		String todo = fqdn;

		File base = new File(baseFolder, source);
		int dotIndex = todo.indexOf('.');
		String path = "";

		while (dotIndex != -1) {
			String segment = todo.substring(0, dotIndex);
			if (path.isEmpty()) {
				path = segment;
			} else {
				path = String.format("%s%s%s", path, FILE_SEPARATOR, segment);
			}

			todo = todo.substring(dotIndex + 1);

			dotIndex = todo.indexOf('.');
		}

		String filename = todo;

		File target = new File(new File(base, path), filename.concat(".java"));

		TypedActionResult<AnalyzedSourceFile> result = new ClassAnalyzer(target)
				.analyze();

		if (!result.isOk()) {
			return result;
		}

		AnalyzedSourceFile sourceFile = result.getObject();

		for (String disallowed : DISALLOWED_IMPORTS) {
			if (sourceFile.hasImport(disallowed)) {
				return TypedActionResult
						.fail("Source file has disallowed import %s (there should be a wildcard or classname after this)",
								disallowed);
			}
		}

		for (String disallowed : disallowedPackages) {
			if (sourceFile.hasImport(disallowed)) {
				return TypedActionResult
						.fail("Source file has disallowed import %s (there should be a wildcard or classname after this)",
								disallowed);
			}
		}

		return result;
	}

	@After
	public void tearDownContext() {
		deleteTree(baseFolder);
	}

	public static MavenContextBuilder mavenContext() {
		return new MavenContextBuilder();
	}

	public static ModularMavenContextBuilder withModule(String name) {
		return new ModularMavenContextBuilder(name, new LinkedList<String>(),
				new LinkedList<DummyFile>());
	}

	protected static class ModularMavenContextBuilder {
		private final List<String> foldersToCreate;

		private final List<DummyFile> dummiesToCreate;

		private final String currentModuleName;

		public ModularMavenContextBuilder(String currentModuleName,
				List<String> foldersToCreate, List<DummyFile> dummiesToCreate) {
			this.currentModuleName = currentModuleName;
			this.foldersToCreate = foldersToCreate;
			this.dummiesToCreate = dummiesToCreate;
		}

		public ModularMavenContextFinalizer withContents(
				MavenContextBuilder builder) {
			DummyProjectContext context = builder.create();
			for (String folder : context.getFoldersToCreate()) {
				foldersToCreate.add(asFolder(currentModuleName.concat(".")
						.concat(folder)));
			}
			for (DummyFile dummyFile : context.getDummyFilesToCreate()) {
				dummiesToCreate.add(new DummyFile(currentModuleName.concat(".")
						.concat(dummyFile.getPackageName()), dummyFile
						.getName(), dummyFile.getSource()));
			}
			return new ModularMavenContextFinalizer(foldersToCreate,
					dummiesToCreate);
		}
	}

	protected static class ModularMavenContextFinalizer {
		private final List<String> foldersToCreate;

		private final List<DummyFile> dummiesToCreate;

		public ModularMavenContextFinalizer(List<String> foldersToCreate,
				List<DummyFile> dummiesToCreate) {
			this.foldersToCreate = foldersToCreate;
			this.dummiesToCreate = dummiesToCreate;
		}

		public DummyProjectContext create() {
			return new DummyProjectContext(foldersToCreate, dummiesToCreate);
		}

		public ModularMavenContextBuilder andModule(String name) {
			return new ModularMavenContextBuilder(name, foldersToCreate,
					dummiesToCreate);
		}
	}

	protected static class MavenContextBuilder {
		private static final String SRC_MAIN_JAVA = "src.main.java";

		private static final String SRC_TEST_JAVA = "src.test.java";

		private static final String SRC_MAIN_RESOURCES = "src.main.resources";

		private static final String SRC_TEST_RESOURCES = "src.test.resources";

		private final List<String> packages;

		private final List<String> testPackages;

		private final List<String> resourcePackages;

		private final List<String> testResourcePackages;

		private final List<DummyFile> files;

		private MavenContextBuilder() {
			this.packages = Lists.newArrayList();
			this.testPackages = Lists.newArrayList();
			this.resourcePackages = Lists.newArrayList();
			this.testResourcePackages = Lists.newArrayList();
			this.files = Lists.newArrayList();
		}

		public MavenContextBuilder withPackage(String packageName) {
			packages.add(packageName);
			return this;
		}

		public MavenContextBuilder withTestPackage(String packageName) {
			testPackages.add(packageName);
			return this;
		}

		public ResourceBuilder withResource(String resourceName) {
			return new ResourceBuilder(resourceName, this);
		}

		public SelectPackage withMainFile(String fileName) {
			return new SelectPackage(this, fileName, SRC_MAIN_JAVA);
		}

		public DummyProjectContext create() {
			List<String> folders = Lists.newArrayListWithExpectedSize((packages
					.isEmpty() ? 0 : packages.size() + 1)
					+ (testPackages.isEmpty() ? 0 : testPackages.size() + 1));
			if (!packages.isEmpty()) {
				folders.add(asFolder(SRC_MAIN_JAVA));
				packages.forEach(p -> folders
						.add(joinFolders(SRC_MAIN_JAVA, p)));
			}
			if (!testPackages.isEmpty()) {
				folders.add(asFolder(SRC_TEST_JAVA));
				testPackages.forEach(p -> folders.add(joinFolders(
						SRC_TEST_JAVA, p)));
			}
			if (!resourcePackages.isEmpty()) {
				folders.add(asFolder(SRC_MAIN_RESOURCES));
				resourcePackages.forEach(p -> folders.add(joinFolders(
						SRC_MAIN_RESOURCES, p)));
			}

			if (!testResourcePackages.isEmpty()) {
				folders.add(asFolder(SRC_TEST_RESOURCES));
				testResourcePackages.forEach(p -> folders.add(joinFolders(
						SRC_TEST_RESOURCES, p)));

			}

			return new DummyProjectContext(folders, files);

		}

		public static class ResourceBuilder {
			private final String name;

			private final MavenContextBuilder builder;

			private String relativePkg = "";

			private ResourceBuilder(String name, MavenContextBuilder builder) {
				super();
				this.name = name;
				this.builder = builder;
			}

			public ResourceBuilder inPackage(String pkg) {
				this.relativePkg = pkg;
				return this;
			}

			public MavenContextBuilder containing(String contents) {
				if (relativePkg.isEmpty()) {
					builder.files.add(new DummyFile(SRC_MAIN_RESOURCES, name,
							contents));
					builder.resourcePackages.add(relativePkg);
				} else {
					String pkg = SRC_MAIN_RESOURCES.concat(".").concat(
							relativePkg);
					builder.files.add(new DummyFile(pkg, name, contents));
					builder.resourcePackages.add(pkg);

				}
				return builder;
			}
		}

	}

	protected static class DummyProjectContext {
		private final List<String> foldersToCreate;

		private final List<DummyFile> dummyFilesToCreate;

		private DummyProjectContext(List<String> foldersToCreate,
				List<DummyFile> dummyFilesToCreate) {
			this.foldersToCreate = foldersToCreate;
			this.dummyFilesToCreate = dummyFilesToCreate;
		}

		public List<String> getFoldersToCreate() {
			return foldersToCreate;
		}

		public List<DummyFile> getDummyFilesToCreate() {
			return dummyFilesToCreate;
		}

	}

	protected static class DummyFile {
		private final String packageName;

		private final String name;

		private final String source;

		private DummyFile(String packageName, String name, String source) {
			this.packageName = packageName;
			this.name = name;
			this.source = source;
		}

		public String getName() {
			return name;
		}

		public String getPackageName() {
			return packageName;
		}

		public String getSource() {
			return source;
		}

		@Override
		public String toString() {
			return String.format("File %s in package %s (%d bytes)", name,
					packageName, source.getBytes().length);
		}
	}

	protected static class SelectPackage {
		private final MavenContextBuilder builder;

		private final String name;

		private final String packageBase;

		private SelectPackage(MavenContextBuilder builder, String name,
				String packageBase) {
			this.builder = builder;
			this.name = name;
			this.packageBase = packageBase;
		}

		public CreateSource inPackage(String packageName) {
			return new CreateSource(builder, name, String.format("%s.%s",
					packageBase, packageName));
		}
	}

	public static class CreateSource {
		private final MavenContextBuilder builder;

		private final String name;

		private final String location;

		private CreateSource(MavenContextBuilder builder, String name,
				String location) {
			this.builder = builder;
			this.name = name;
			this.location = location;
		}

		public MavenContextBuilder withStringContents(String contents) {
			DummyFile dummy = new DummyFile(location, name, contents);
			builder.files.add(dummy);
			return builder;
		}

		public MavenContextBuilder withContentsFromDummy(String dummyLocation)
				throws IOException {
			StringBuilder contents = new StringBuilder();
			try (BufferedReader dummyReader = new BufferedReader(
					new FileReader(dummyLocation))) {

				String next;
				while ((next = dummyReader.readLine()) != null) {
					contents.append(next).append(LINE_SEPARATOR);
				}
			}
			DummyFile dummy = new DummyFile(location, name, contents.toString());
			builder.files.add(dummy);
			return builder;
		}
	}

	private static String joinFolders(String base, String pkg) {
		return asFolder(String.format("%s.%s", base, pkg));
	}

	private static String asFolder(String packageNotation) {
		return packageNotation.replace(".", FILE_SEPARATOR);
	}

	private static void deleteTree(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					deleteTree(f);
				}
			}

			file.delete();
		}
	}
}
