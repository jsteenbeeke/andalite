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

import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BooleanValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.CharValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.FieldAccessValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.IntegerValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IAnnotationOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureAnnotationField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureInnerAnnotationField;

public interface IAnnotationOperationBuilder<T extends ILocatable, O extends IJavaOperation<T>>
		extends IScopedOperationBuilder<T, O> {
	String NULL = "null";

	default void ensureBooleanValue(@Nonnull String name, boolean value) {
		ensure(new EnsureAnnotationField<>(name, BooleanValue.class,
										   value) {
			@Override
			public String format(Boolean value) {
				return value != null ? Boolean.toString(value) : NULL;
			}
		});
	}

	default void ensureFieldAccessValue(@Nonnull String name,
			@Nonnull String fieldAccess) {
		ensure(new EnsureAnnotationField<>(name, FieldAccessValue.class,
										   fieldAccess) {
			@Override
			public String format(String value) {
				return value;
			}
		});

	}

	default void ensureStringValue(@Nonnull String name, String value) {
		ensure(new EnsureAnnotationField<>(name, StringValue.class,
										   value) {
			@Override
			public String format(String value) {
				return value != null ? String.format("\"%s\"", value) : NULL;
			}
		});
	}

	default void ensureIntegerValue(@Nonnull String name, Integer value) {
		ensure(new EnsureAnnotationField<>(name, IntegerValue.class,
										   value) {
			@Override
			public String format(Integer value) {
				return value != null ? Integer.toString(value) : NULL;
			}
		});
	}

	default void ensureCharValue(@Nonnull String name, Character value) {
		ensure(new EnsureAnnotationField<>(name, CharValue.class,
										   value) {
			@Override
			public String format(Character value) {
				return value != null ? String.format("'%c'", value) : NULL;
			}
		});
	}

	default EnsureInnerAnnotationField ensureAnnotationValue(
			@Nonnull String name, @Nonnull String type) {
		EnsureInnerAnnotationField field = new EnsureInnerAnnotationField(name,
				type);
		ensure(field);
		return field;
	}

	void ensure(IAnnotationOperation operation);
}
