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
package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;

public abstract class ConstructableDenomination extends ContainingDenomination {

	private final List<AnalyzedConstructor> constructors;

	protected ConstructableDenomination(Location location, int modifiers,
			String packageName, String denominationName) {
		super(location, modifiers, packageName, denominationName);
		this.constructors = Lists.newArrayList();
	}

	void addConstructor(@Nonnull AnalyzedConstructor constructor) {
		this.constructors.add(constructor);
	}

	@Nonnull
	public List<AnalyzedConstructor> getConstructors() {
		return ImmutableList.copyOf(constructors);
	}

}
