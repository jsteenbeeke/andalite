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

package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.operations.ICompilationUnitOperation;

import org.jetbrains.annotations.NotNull;

/**
 * Ensures that a given compilation unit will have a class with default
 * (package) scope and the specified name
 *
 * @author Jeroen Steenbeeke
 */
public class EnsurePackageClass implements ICompilationUnitOperation {
	private final String expectedClassName;

	/**
	 * Create a new EnsurePackageClass operation
	 *
	 * @param expectedClassName The name of the class we want to ensure the existence of.
	 *                          Needs to be a valid Java identifier
	 */
	public EnsurePackageClass(String expectedClassName) {
		if (!isValidJavaIdentifier(expectedClassName)) {
			throw new IllegalArgumentException(String.format(
				"%s is not a valid Java identifier", expectedClassName));
		}
		this.expectedClassName = expectedClassName;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedSourceFile input) {
		if (input
			.getClasses()
			.stream()
			.filter(c -> c.getAccessModifier() == AccessModifier.DEFAULT)
			.anyMatch(c -> c.getDenominationName().equals(expectedClassName))) {
			return ImmutableList.of();
		}

		if (input
			.getClasses()
			.stream()
			.filter(c -> c.getAccessModifier() == AccessModifier.PUBLIC)
			.anyMatch(c -> c.getDenominationName().equals(expectedClassName))) {
			throw new IllegalStateException(String.format("Class named %s is present, but is public", expectedClassName));
		}

		return ImmutableList
			.of(input.insertAt(AnalyzedSourceFile.SourceFileInsertionPoint.AFTER_LAST_DENOMINATION,
							   String.format("class %s {\n\n}\n", expectedClassName)));
	}

	@Override
	public String getDescription() {
		return String.format("presence of a package-level class named %s",
							 expectedClassName);
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedSourceFile input) {
		for (AnalyzedClass analyzedClass : input.getClasses()) {
			if (analyzedClass.getAccessModifier() == AccessModifier.DEFAULT
				&& expectedClassName.equals(analyzedClass.getClassName())) {
				return ActionResult.ok();
			}
		}
		return ActionResult.error("No package class named %s",
								  expectedClassName);
	}
}
