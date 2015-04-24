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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedField extends AccessModifiable {
	private final String name;

	private final AnalyzedType type;

	private Location specificDeclarationLocation;

	private AnalyzedExpression initializationExpression;

	public AnalyzedField(@Nonnull Location location, int modifiers,
			@Nonnull String name, @Nonnull AnalyzedType type) {
		super(location, modifiers);
		this.name = name;
		this.type = type;
		this.specificDeclarationLocation = location;
		this.initializationExpression = null;
	}

	public Location getSpecificDeclarationLocation() {
		return specificDeclarationLocation;
	}

	void setSpecificDeclarationLocation(Location specificDeclarationLocation) {
		this.specificDeclarationLocation = specificDeclarationLocation;
	}

	void setInitializationExpression(AnalyzedExpression initializationExpression) {
		this.initializationExpression = initializationExpression;
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public AnalyzedType getType() {
		return type;
	}

	@CheckForNull
	public AnalyzedExpression getInitializationExpression() {
		return initializationExpression;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write(type.toJavaString());
		callback.write(" ");
		callback.write(name);
		callback.write(";");
	}
}
