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

package com.jeroensteenbeeke.andalite.transformation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.transformation.navigation.INavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.PackageClassNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.PublicClassNavigation;

public class ClassLocator {
	private final INavigation<AnalyzedClass> navigation;

	private ClassLocator(@Nonnull INavigation<AnalyzedClass> navigation) {
		this.navigation = navigation;
	}

	@Nonnull
	INavigation<AnalyzedClass> getNavigation() {
		return navigation;
	}

	@Nonnull
	public static ClassLocator publicClass() {
		return new ClassLocator(new PublicClassNavigation());
	}

	@Nonnull
	public static ClassLocator packageClassNamed(@Nonnull String name) {
		return new ClassLocator(new PackageClassNavigation(name));
	}
}
