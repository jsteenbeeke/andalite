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

import com.github.javaparser.ast.Modifier;

import java.util.List;

public enum AccessModifier {
	PUBLIC, DEFAULT {
		@Override
		public String getOutput() {
			return "";
		}
	},
	PROTECTED, PRIVATE;

	public String getOutput() {
		return String.format("%s ", name().toLowerCase());
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	public static AccessModifier fromModifiers(List<Modifier.Keyword> modifiers) {
		if (modifiers.contains(Modifier.Keyword.PRIVATE)) {
			return PRIVATE;
		}
		if (modifiers.contains(Modifier.Keyword.PROTECTED)) {
			return PROTECTED;
		}
		if (modifiers.contains(Modifier.Keyword.PUBLIC)) {
			return PUBLIC;
		}

		return DEFAULT;
	}
}
