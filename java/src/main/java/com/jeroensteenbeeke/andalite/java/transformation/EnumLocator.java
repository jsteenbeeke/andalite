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

package com.jeroensteenbeeke.andalite.java.transformation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PackageEnumNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PublicEnumNavigation;

public class EnumLocator {
	private final IJavaNavigation<AnalyzedEnum> navigation;

	private EnumLocator(@Nonnull IJavaNavigation<AnalyzedEnum> navigation) {
		this.navigation = navigation;
	}

	@Nonnull
	IJavaNavigation<AnalyzedEnum> getNavigation() {
		return navigation;
	}

	@Nonnull
	public static EnumLocator publicEnum() {
		return new EnumLocator(new PublicEnumNavigation());
	}

	@Nonnull
	public static EnumLocator packageEnumNamed(@Nonnull String name) {
		return new EnumLocator(new PackageEnumNavigation(name));
	}
}
