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

package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.ILocatable;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.ICompilationUnitOperation;

public class EnsurePackageClass implements ICompilationUnitOperation {
	private final String expectedClassName;

	public EnsurePackageClass(String expectedClassName) {
		super();
		this.expectedClassName = expectedClassName;
	}

	@Override
	public List<Transformation> perform(AnalyzedSourceFile input) {
		ILocatable last = input;

		for (AnalyzedClass analyzedClass : input.getClasses()) {
			if (analyzedClass.getAccessModifier() == AccessModifier.DEFAULT
					&& expectedClassName.equals(analyzedClass.getClassName())) {
				return ImmutableList.of();
			}

			last = analyzedClass;
		}

		return ImmutableList.of(Transformation.insertAfter(last,
				String.format("class %s {\n\n}\n", expectedClassName)));
	}

	@Override
	public String getDescription() {
		return String.format("presence of a package-level class named %s",
				expectedClassName);
	}
}
