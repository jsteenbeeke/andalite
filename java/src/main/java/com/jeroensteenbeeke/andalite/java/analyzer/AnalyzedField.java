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

package com.jeroensteenbeeke.andalite.java.analyzer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of a field within a denomination
 * 
 * @author Jeroen Steenbeeke
 */
public final class AnalyzedField extends AccessModifiable {
	private final String name;

	private final AnalyzedType type;

	private Location specificDeclarationLocation;

	private AnalyzedExpression initializationExpression;

	/**
	 * Create a new AnalyzedField
	 * 
	 * @param location
	 *            The location of the field
	 * @param modifiers
	 *            The modifiers of the field, indicating what keywords it
	 *            has
	 * @param name
	 *            The name of the field
	 * @param type
	 *            The type of the field
	 */
	AnalyzedField(@Nonnull Location location, int modifiers,
			@Nonnull String name, @Nonnull AnalyzedType type) {
		super(location, modifiers);
		this.name = name;
		this.type = type;
		this.specificDeclarationLocation = location;
		this.initializationExpression = null;
	}

	/**
	 * Java has the possibility to declare multiple variables in a single
	 * statement. In this case, the
	 * AnalyzedField's location will represent the total statement's location,
	 * which is copied by each AnalyzedField resulting from this declaration.
	 * 
	 * The specific location is the unambiguous location where this exact field
	 * is defined
	 * 
	 * @return The specific location of this field
	 */
	public Location getSpecificDeclarationLocation() {
		return specificDeclarationLocation;
	}

	/**
	 * Sets the unambiguous location of this field's declaration
	 * 
	 * @param specificDeclarationLocation
	 *            The location of this field
	 */
	void setSpecificDeclarationLocation(
			@Nonnull Location specificDeclarationLocation) {
		this.specificDeclarationLocation = specificDeclarationLocation;
	}

	/**
	 * Get the initializing expression that determines this field's initial
	 * value
	 * 
	 * @return The expression that initializes this variable, or {@code null} if
	 *         no such expression exists
	 */
	@CheckForNull
	public AnalyzedExpression getInitializationExpression() {
		return initializationExpression;
	}

	/**
	 * Sets the initializer expression for this field
	 * 
	 * @param initializationExpression
	 *            The expression that is assigned to this field as initializer
	 */
	void setInitializationExpression(
			@Nonnull AnalyzedExpression initializationExpression) {
		this.initializationExpression = initializationExpression;
	}

	/**
	 * Get the name of the field
	 * 
	 * @return The name of the field
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * Get the type of the field
	 * 
	 * @return The type of the field
	 */
	@Nonnull
	public AnalyzedType getType() {
		return type;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write(type.toJavaString());
		callback.write(" ");
		callback.write(name);
		callback.write(";");
	}
}
