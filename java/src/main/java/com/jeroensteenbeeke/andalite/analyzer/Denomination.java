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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;

public abstract class Denomination extends AccessModifiable {

	private final String packageName;

	private final String denominationName;

	public Denomination(@Nonnull Location location, int modifiers,
			@Nonnull String packageName, @Nonnull String denominationName) {
		super(location, modifiers);
		this.packageName = packageName;
		this.denominationName = denominationName;

	}

	public String getDenominationName() {
		return denominationName;
	}

	public String getPackageName() {
		return packageName;
	}
}
