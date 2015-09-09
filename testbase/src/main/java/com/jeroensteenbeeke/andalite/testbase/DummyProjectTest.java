package com.jeroensteenbeeke.andalite.testbase;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

public abstract class DummyProjectTest {
	private static final Logger log = LoggerFactory.getLogger(DummyProjectTest.class);
	
	private static final String SRC_MAIN_JAVA = "src/main/java";

	public static final String KEY_SOURCE_PATH = "sourcePath";

	private static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private final DummyProjectContext context;

	private File baseFolder;

	protected DummyProjectTest(DummyProjectContext context) {
		this.context = context;
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

		baseFolder = Files.createTempDir();
		context.getFoldersToCreate().forEach(c -> {
			if (!success.get()) {
				return;
			}

			File f = new File(baseFolder, c);
			if (c.contains(FILE_SEPARATOR)) {
				if (!f.mkdirs()) {
					success.set(false);
				} else {
					log.info("Created folder {}", f.getAbsolutePath());
				}
			} else {
				if (!f.mkdir()) {
					success.set(false);
				} else {
					log.info("Created folder {}", f.getAbsolutePath());
				}
			}

			if (packageDummies.containsKey(c)) {
				packageDummies.get(c).forEach(d -> {
					File dummy = new File(f, d.getName());
					try (FileOutputStream fos = new FileOutputStream(dummy)) {
						for (char e : d.getSource().toCharArray()) {
							fos.write(e);
						}
						fos.flush();
						log.info("Created file {}", dummy.getAbsolutePath());
					} catch (IOException ioe) {
						success.set(false);
					}
				});
			}
		});

		assertTrue(success.get());
	}

	@After
	public void tearDownContext() {
		deleteTree(baseFolder);
	}

	public static MavenContextBuilder mavenContext() {
		return new MavenContextBuilder();
	}

	protected static class MavenContextBuilder {
		private static final String SRC_MAIN_JAVA = "src.main.java";

		private static final String SRC_TEST_JAVA = "src.test.java";

		private final List<String> packages;

		private final List<String> testPackages;

		private final List<DummyFile> files;

		private MavenContextBuilder() {
			this.packages = Lists.newArrayList();
			this.testPackages = Lists.newArrayList();
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

			return new DummyProjectContext(folders, files);

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
