package com.jeroensteenbeeke.andalite.analyzer;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedSourceFile extends Locatable {
	private final List<AnalyzedClass> classes;

	private final List<AnalyzedImport> imports;

	private final String packageName;

	private final Location packageDefinitionLocation;

	private final File originalFile;

	public AnalyzedSourceFile(@Nonnull Location location,
			@Nonnull File originalFile, @Nonnull String packageName,
			@Nonnull Location packageDefinitionLocation) {
		super(location);
		this.originalFile = originalFile;
		this.packageName = packageName;
		this.packageDefinitionLocation = packageDefinitionLocation;
		this.classes = Lists.newArrayList();
		this.imports = Lists.newArrayList();
	}

	public Location getPackageDefinitionLocation() {
		return packageDefinitionLocation;
	}

	@Nonnull
	public String getPackageName() {
		return packageName;
	}

	@Nonnull
	public List<AnalyzedClass> getClasses() {
		return ImmutableList.copyOf(classes);
	}

	@Nonnull
	public List<AnalyzedImport> getImports() {
		return ImmutableList.copyOf(imports);
	}

	public boolean hasImport(@Nonnull String fqdn) {
		for (AnalyzedImport analyzedImport : imports) {
			if (analyzedImport.matchesClass(fqdn)) {
				return true;
			}
		}

		return false;
	}

	void addClass(@Nonnull AnalyzedClass analyzedClass) {
		this.classes.add(analyzedClass);
	}

	void addImport(@Nonnull AnalyzedImport importStatement) {
		this.imports.add(importStatement);
	}

	@Override
	public void output(OutputCallback callback) {
		callback.write("package ");
		callback.write(packageName);
		callback.write(";");
		callback.newline();
		callback.newline();
		for (AnalyzedImport analyzedImport : getImports()) {
			analyzedImport.output(callback);
		}

		callback.newline();
		for (AnalyzedClass analyzedClass : getClasses()) {
			analyzedClass.output(callback);
		}

	}

	public File getOriginalFile() {
		return originalFile;
	}
}
