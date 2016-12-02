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

/**
 * Representation of a Java source file
 * 
 * @author Jeroen Steenbeeke
 */
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

	/**
	 * Create a new AnalyzedSourceFile
	 * 
	 * @param location
	 *            The location of the source file (generally speaking this is
	 *            the full length of the file)
	 * @param originalFile
	 *            The file this representation was parsed from
	 * @param packageName
	 *            The name of the package this class file is in
	 * @param packageDefinitionLocation
	 *            The location of the package definition
	 */
	AnalyzedSourceFile(@Nonnull Location location, @Nonnull File originalFile,
			@Nonnull String packageName,
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

	@Nonnull
	private static String extractCompilationUnitName(@Nonnull File file) {
		final String fileName = file.getName();

		return fileName.substring(0, fileName.length() - 5);
	}

	/**
	 * Get the name of the compilation unit. This is generally the name of the
	 * primary class, interface, annotation or enum defined in this file
	 * 
	 * @return The compilation unit name
	 */
	@Nonnull
	public String getCompilationUnitName() {
		return compilationUnitName;
	}

	/**
	 * Get the location of the package declaration
	 * 
	 * @return The location of the package declaration
	 */
	@Nonnull
	public Location getPackageDefinitionLocation() {
		return packageDefinitionLocation;
	}

	/**
	 * Get the packagename of this source file
	 * 
	 * @return The name of the package
	 */
	@Nonnull
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Get all classes defined in this source file
	 * 
	 * @return An immutable list of classes
	 */
	@Nonnull
	public List<AnalyzedClass> getClasses() {
		return ImmutableList.copyOf(classes);
	}

	/**
	 * Get all annotations defined in this source file
	 * 
	 * @return An immutable list of annotations
	 */
	@Nonnull
	public List<AnalyzedAnnotationType> getAnnotations() {
		return ImmutableList.copyOf(annotations);
	}

	/**
	 * Get all enums defined in this source file
	 * 
	 * @return An immutable list of enums
	 */
	@Nonnull
	public List<AnalyzedEnum> getEnums() {
		return ImmutableList.copyOf(enums);
	}

	/**
	 * Get all interfaces defined in this source file
	 * 
	 * @return An immutable list of interfaces
	 */
	@Nonnull
	public List<AnalyzedInterface> getInterfaces() {
		return ImmutableList.copyOf(interfaces);
	}

	/**
	 * Get a list of all imports made in this source file
	 * 
	 * @return An immutable list of imports
	 */
	@Nonnull
	public List<AnalyzedImport> getImports() {
		return ImmutableList.copyOf(imports);
	}

	/**
	 * Get the original file this representation was parsed from
	 * 
	 * @return The file
	 */
	@Nonnull
	public File getOriginalFile() {
		return originalFile;
	}

	/**
	 * Checks if this source file has an import of the given fully qualified
	 * domain name
	 * 
	 * @param fqdn
	 *            The fully qualified domain name to check for
	 * @return {@code true} if the import is present in this file, {@code false}
	 *         otherwise
	 */
	public boolean hasImport(@Nonnull String fqdn) {
		return imports.stream().anyMatch(i -> i.matchesClass(fqdn));
	}

	/**
	 * Add a class to this source file
	 * 
	 * @param analyzedClass
	 *            the class to add
	 */
	void addClass(@Nonnull AnalyzedClass analyzedClass) {
		this.classes.add(analyzedClass);
	}

	/**
	 * Add an interface to this source file
	 * 
	 * @param analyzedInterface
	 *            the interface to add
	 */
	void addInterface(@Nonnull AnalyzedInterface analyzedInterface) {
		this.interfaces.add(analyzedInterface);
	}

	/**
	 * Add an enum to this source file
	 * 
	 * @param analyzedEnum
	 *            the enum to add
	 */
	void addEnum(@Nonnull AnalyzedEnum analyzedEnum) {
		this.enums.add(analyzedEnum);
	}

	/**
	 * Add an annotation to this source file
	 * 
	 * @param annotation
	 *            the annotation to add
	 */
	void addAnnotation(@Nonnull AnalyzedAnnotationType annotation) {
		this.annotations.add(annotation);
	}

	/**
	 * Add an import to this source file
	 * 
	 * @param importStatement
	 *            The import statement to add
	 */
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
}
