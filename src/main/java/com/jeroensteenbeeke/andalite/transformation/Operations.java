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

import com.jeroensteenbeeke.andalite.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BooleanValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.CharValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.IntegerValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.transformation.operations.AnnotationOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.CompilationUnitOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.FieldOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.*;

public class Operations {

	private static final String NULL = "null";

	private Operations() {

	}

	public static CompilationUnitOperation hasPublicClass() {
		return new EnsurePublicClass();
	}

	public static CompilationUnitOperation imports(@Nonnull String fqdn) {
		return new EnsureImports(fqdn);
	}

	public static ClassOperation hasClassAnnotation(@Nonnull String annotation) {
		return new EnsureClassAnnotation(annotation);
	}

	public static FieldOperation hasFieldAnnotation(@Nonnull String annotation) {
		return new EnsureFieldAnnotation(annotation);
	}

	public static EnsureMethodBuilder hasMethod() {
		return new EnsureMethodBuilder();
	}

	public static AnnotationOperation hasBooleanValue(@Nonnull String name,
			boolean value) {
		return new EnsureAnnotationField<Boolean>(name, BooleanValue.class,
				value) {
			@Override
			public String format(Boolean value) {
				return value != null ? Boolean.toString(value) : NULL;
			}
		};
	}

	public static AnnotationOperation hasStringValue(@Nonnull String name,
			String value) {
		return new EnsureAnnotationField<String>(name, StringValue.class, value) {
			@Override
			public String format(String value) {
				return value != null ? String.format("\"%s\"", value) : NULL;
			}
		};
	}

	public static AnnotationOperation hasIntegerValue(@Nonnull String name,
			Integer value) {
		return new EnsureAnnotationField<Integer>(name, IntegerValue.class,
				value) {
			@Override
			public String format(Integer value) {
				return value != null ? Integer.toString(value) : NULL;
			}
		};
	}

	public static AnnotationOperation hasCharValue(@Nonnull String name,
			Character value) {
		return new EnsureAnnotationField<Character>(name, CharValue.class,
				value) {
			@Override
			public String format(Character value) {
				return value != null ? String.format("'%c'", value) : NULL;
			}
		};
	}

	public static EnsureInnerAnnotationField hasAnnotationValue(
			@Nonnull String name, @Nonnull String type) {
		return new EnsureInnerAnnotationField(name, type);
	}

	public static HasFieldBuilderName hasField(@Nonnull String name) {
		return new HasFieldBuilderName(name);
	}

	public static class HasFieldBuilderName {
		private final String name;

		private HasFieldBuilderName(@Nonnull String name) {
			super();
			this.name = name;
		}

		public HasFieldBuilderNameType typed(@Nonnull String type) {
			return new HasFieldBuilderNameType(name, type);
		}
	}

	public static class HasFieldBuilderNameType {
		private final String name;

		private final String type;

		private HasFieldBuilderNameType(@Nonnull String name,
				@Nonnull String type) {
			super();
			this.name = name;
			this.type = type;
		}

		public ClassOperation withAccess(@Nonnull AccessModifier modifier) {
			return new EnsureField(name, type, modifier);
		}
	}
}
