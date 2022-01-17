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

package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;

public class PackageClassNavigation implements IJavaNavigation<AnalyzedClass> {
	private final String name;

	public PackageClassNavigation(@NotNull String name) {
		this.name = name;
	}

	@Override
	@NotNull
	public AnalyzedClass navigate(AnalyzedSourceFile file)
			throws NavigationException {
		List<String> found = Lists.newArrayList();

		for (AnalyzedClass analyzedClass : file.getClasses()) {
			if (analyzedClass.getAccessModifier() == AccessModifier.DEFAULT) {
				if (name.equals(analyzedClass.getClassName())) {
					return analyzedClass;
				} else {
					found.add("class ".concat(analyzedClass.getClassName()));
				}
			} else {
				found.add("public class ".concat(analyzedClass.getClassName()));
			}

		}

		throw new NavigationException(
				"Source file has no package class named %s (found: %s)", name,
				found.stream().collect(Collectors.joining(", ")));
	}

	@Override
	@NotNull
	public String getDescription() {
		return String.format("Go to package class %s", name);
	}

}
