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

package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.operations.ICompilationUnitOperation;

/**
 * Ensures that a given compilation unit will have a enum with default
 * (package) scope and the specified name
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class EnsurePackageEnum implements ICompilationUnitOperation {
	private final String expectedEnumName;

	/**
	 * Create a new EnsurePackageEnum operation
	 * 
	 * @param expectedEnumName
	 *            The name of the class we want to ensure the existence of.
	 *            Needs to be a valid Java identifier
	 */
	public EnsurePackageEnum(String expectedEnumName) {
		if (!isValidJavaIdentifier(expectedEnumName)) {
			throw new IllegalArgumentException(String.format(
					"%s is not a valid Java identifier", expectedEnumName));
		}
		this.expectedEnumName = expectedEnumName;
	}

	@Override
	public List<Transformation> perform(AnalyzedSourceFile input) {
		for (AnalyzedEnum analyzedEnum : input.getEnums()) {
			if (analyzedEnum.getAccessModifier() == AccessModifier.DEFAULT
					&& expectedEnumName.equals(analyzedEnum.getEnumName())) {
				return ImmutableList.of();
			}
		}

		return ImmutableList
				.of(input.insertAt(AnalyzedSourceFile.SourceFileInsertionPoint.AFTER_LAST_DENOMINATION,
						String.format("enum %s {\n\n}\n", expectedEnumName)));
	}

	@Override
	public String getDescription() {
		return String.format("presence of a package-level class named %s",
				expectedEnumName);
	}

	@Override
	public ActionResult verify(AnalyzedSourceFile input) {
		for (AnalyzedEnum analyzedEnum : input.getEnums()) {
			if (analyzedEnum.getAccessModifier() == AccessModifier.DEFAULT
					&& expectedEnumName.equals(analyzedEnum.getEnumName())) {
				return ActionResult.ok();
			}
		}
		return ActionResult.error("No package enum named %s", expectedEnumName);
	}
}
