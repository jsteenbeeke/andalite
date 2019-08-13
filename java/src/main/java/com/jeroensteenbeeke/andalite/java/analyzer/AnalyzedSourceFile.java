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

package com.jeroensteenbeeke.andalite.java.analyzer;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.javaparser.Range;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.*;

public final class AnalyzedSourceFile extends Locatable implements IInsertionPointProvider<AnalyzedSourceFile, AnalyzedSourceFile.SourceFileInsertionPoint> {
	private final List<AnalyzedClass> classes;

	private final List<AnalyzedInterface> interfaces;

	private final List<AnalyzedAnnotationType> annotations;

	private final List<AnalyzedEnum> enums;

	private final List<AnalyzedImport> imports;

	private final String packageName;

	private final Location packageDefinitionLocation;

	private final File originalFile;

	private final String compilationUnitName;

	public AnalyzedSourceFile(@Nonnull Location location,
							  @Nonnull File originalFile, @Nullable String packageName,
							  @Nullable Location packageDefinitionLocation) {
		super(location);
		this.originalFile = originalFile;
		this.compilationUnitName = extractCompilationUnitName(originalFile);
		this.packageName = Optional.ofNullable(packageName).orElse("");
		this.packageDefinitionLocation = packageDefinitionLocation;
		this.classes = Lists.newArrayList();
		this.interfaces = Lists.newArrayList();
		this.enums = Lists.newArrayList();
		this.annotations = Lists.newArrayList();
		this.imports = Lists.newArrayList();
	}

	private static String extractCompilationUnitName(File file) {
		final String fileName = file.getName();

		return fileName.substring(0, fileName.length() - 5);
	}

	public String getCompilationUnitName() {
		return compilationUnitName;
	}

	@Nonnull
	public Optional<Location> getPackageDefinitionLocation() {
		return Optional.ofNullable(packageDefinitionLocation);
	}

	@Nonnull
	public String getPackageName() {
		return packageName;
	}

	@Nonnull
	public List<AnalyzedClass> getClasses() {
		return ImmutableList.copyOf(classes);
	}

	public List<AnalyzedAnnotationType> getAnnotations() {
		return ImmutableList.copyOf(annotations);
	}

	public List<AnalyzedEnum> getEnums() {
		return ImmutableList.copyOf(enums);
	}

	public List<AnalyzedInterface> getInterfaces() {
		return ImmutableList.copyOf(interfaces);
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

	void addInterface(@Nonnull AnalyzedInterface analyzedInterface) {
		this.interfaces.add(analyzedInterface);
	}

	void addEnum(@Nonnull AnalyzedEnum analyzedEnum) {
		this.enums.add(analyzedEnum);
	}

	void addAnnotation(@Nonnull AnalyzedAnnotationType analyzedType) {
		this.annotations.add(analyzedType);
	}

	void addImport(@Nonnull AnalyzedImport importStatement) {
		this.imports.add(importStatement);
	}

	@Override
	public void output(IOutputCallback callback) {
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

	public String getFullyQualifiedName() {
		return packageName != null && !packageName.isBlank() ? packageName + "." + compilationUnitName : compilationUnitName;
	}

	public enum SourceFileInsertionPoint implements IInsertionPoint<AnalyzedSourceFile> {
		AFTER_PACKAGE_DECLARATION {
			@Override
			public int position(AnalyzedSourceFile container) {
				return container.getPackageDefinitionLocation().map(l -> l.getEnd() + 1).orElse(0);
			}
		}, AFTER_IMPORTS {
			@Override
			public int position(AnalyzedSourceFile container) {
				return container
					.getImports()
					.stream()
					.map(AnalyzedImport::getLocation)
					.reduce(Location::max)
					.map(Location::getEnd)
					.map(e -> e + 1)
					.orElseGet(() -> AFTER_PACKAGE_DECLARATION.position(container));
			}
		}, AFTER_LAST_DENOMINATION {
			@Override
			public int position(AnalyzedSourceFile container) {
				return ImmutableList.<Denomination<?,?>>builder()
					.addAll(container.getClasses())
					.addAll(container.getEnums())
					.addAll(container.getAnnotations())
					.addAll(container.getInterfaces())
					.build().stream()
					.map(Denomination::getLocation)
					.reduce(Location::max)
					.map(Location::getEnd)
					.map(e -> e + 1)
					.orElseGet(() -> AFTER_IMPORTS.position(container));
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		AnalyzedSourceFile that = (AnalyzedSourceFile) o;
		return originalFile.equals(that.originalFile);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), originalFile);
	}
}
