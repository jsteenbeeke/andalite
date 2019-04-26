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
package com.jeroensteenbeeke.andalite.java.analyzer.types;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;

public class Primitive extends AnalyzedType {
	private final PrimitiveType type;

	public Primitive(@Nonnull final Location location,
			@Nonnull final PrimitiveType type) {
		super(location);
		this.type = type;
	}

	public PrimitiveType getType() {
		return type;
	}

	@Override
	public String toJavaString() {
		return type.name().toLowerCase();
	}

	public enum PrimitiveType {
		Boolean, Byte, Char, Double, Float, Int, Long, Short;

		public static PrimitiveType fromParserType(
				com.github.javaparser.ast.type.PrimitiveType.Primitive prim) {
			switch (prim) {
				case BOOLEAN:
					return Boolean;
				case BYTE:
					return Byte;
				case CHAR:
					return Char;
				case DOUBLE:
					return Double;
				case FLOAT:
					return Float;
				case INT:
					return Int;
				case LONG:
					return Long;
				case SHORT:
					return Short;
			}

			throw new IllegalArgumentException(String.format(
					"Unrecognized primitive type %s", prim));
		}
	}

}
