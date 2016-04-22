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

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;

public class PackageEnumNavigation implements IJavaNavigation<AnalyzedEnum> {
	private final String name;

	public PackageEnumNavigation(@Nonnull String name) {
		this.name = name;
	}

	@Override
	@Nonnull
	public AnalyzedEnum navigate(AnalyzedSourceFile file)
			throws NavigationException {
		List<String> found = Lists.newArrayList();

		for (AnalyzedEnum analyzedEnum : file.getEnums()) {
			if (analyzedEnum.getAccessModifier() == AccessModifier.DEFAULT) {
				if (name.equals(analyzedEnum.getEnumName())) {
					return analyzedEnum;
				} else {
					found.add("enum ".concat(analyzedEnum.getEnumName()));
				}
			} else {
				found.add("public enum ".concat(analyzedEnum.getEnumName()));
			}

		}

		throw new NavigationException(
				"Source file has no package class named %s (found: %s)", name,
				found.stream().collect(Collectors.joining(", ")));
	}

	@Override
	@Nonnull
	public String getDescription() {
		return String.format("Go to package enum %s", name);
	}

}
