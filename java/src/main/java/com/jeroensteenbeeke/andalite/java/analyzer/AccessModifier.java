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

import javax.annotation.Nonnull;

import com.github.antlrjavaparser.api.body.ModifierSet;

/**
 * Enum representing the default Java access modifiers
 * 
 * @author Jeroen Steenbeeke
 *
 */
public enum AccessModifier {
	PUBLIC, DEFAULT {
		@Override
		public String getOutput() {
			return "";
		}
	},
	PROTECTED, PRIVATE;

	/**
	 * Renders the access modifier as it would be outputted in a definition
	 * 
	 * @return The access modifier with a trailing space
	 */
	@Nonnull
	public String getOutput() {
		return String.format("%s ", name().toLowerCase());
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	/**
	 * Determines the AccessModifier based on the modifiers property used by
	 * antlr-java-parser
	 * 
	 * @param modifiers
	 *            The modifiers as specified by antlr-java-parser
	 * @return The AccessModifier corresponding to the given modifiers
	 */
	@Nonnull
	public static AccessModifier fromModifiers(int modifiers) {
		if (ModifierSet.isPrivate(modifiers)) {
			return PRIVATE;
		}
		if (ModifierSet.isProtected(modifiers)) {
			return PROTECTED;
		}
		if (ModifierSet.isPublic(modifiers)) {
			return PUBLIC;
		}

		return DEFAULT;
	}
}
