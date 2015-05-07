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

package com.jeroensteenbeeke.andalite.java.analyzer;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;

public final class AnalyzedSourceFile extends Locatable {
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
			@Nonnull File originalFile, @Nonnull String packageName,
			@Nonnull Location packageDefinitionLocation) {
		super(location);
		this.originalFile = originalFile;
		this.compilationUnitName = extractCompilationUnitName(originalFile);
		this.packageName = packageName;
		this.packageDefinitionLocation = packageDefinitionLocation;
		this.classes = Lists.newArrayList();
		this.interfaces = Lists.newArrayList();
		this.enums = Lists.newArrayList();
		this.annotations = Lists.newArrayList();
		this.imports = Lists.newArrayList();
	}

	private static String extractCompilationUnitName(File file) {
		final String fileName = file.getName();
		
		return fileName.substring(0, fileName.length()-5);
	}

	public String getCompilationUnitName() {
		return compilationUnitName;
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
}
