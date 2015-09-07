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

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BooleanValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.CharValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.FieldAccessValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.IntegerValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.java.transformation.operations.*;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.*;

public class Operations {

	private static final String NULL = "null";

	private Operations() {

	}

	public static IBodyContainerOperation returnsAsLastStatement(
			@Nonnull String returnValue) {
		return new EnsureEndReturnStatement(returnValue);
	}

	public static IBodyContainerOperation hasStatement(@Nonnull String statement) {
		return new EnsureStatement(statement);
	}

	public static ICompilationUnitOperation hasPublicClass() {
		return new EnsurePublicClass();
	}

	public static ICompilationUnitOperation imports(@Nonnull String fqdn) {
		return new EnsureImports(fqdn);
	}

	public static IClassOperation hasClassAnnotation(@Nonnull String annotation) {
		return new EnsureClassAnnotation(annotation);
	}

	public static IFieldOperation hasFieldAnnotation(@Nonnull String annotation) {
		return new EnsureFieldAnnotation(annotation);
	}

	public static EnsureMethodBuilder hasMethod() {
		return new EnsureMethodBuilder();
	}

	public static IAnnotationOperation hasBooleanValue(@Nonnull String name,
			boolean value) {
		return new EnsureAnnotationField<Boolean>(name, BooleanValue.class,
				value) {
			@Override
			public String format(Boolean value) {
				return value != null ? Boolean.toString(value) : NULL;
			}
		};
	}

	public static IAnnotationOperation hasFieldAccessValue(
			@Nonnull String name, @Nonnull String fieldAccess) {
		return new EnsureAnnotationField<String>(name, FieldAccessValue.class,
				fieldAccess) {
			@Override
			public String format(String value) {
				return value;
			}
		};

	}

	public static IAnnotationOperation hasStringValue(@Nonnull String name,
			String value) {
		return new EnsureAnnotationField<String>(name, StringValue.class, value) {
			@Override
			public String format(String value) {
				return value != null ? String.format("\"%s\"", value) : NULL;
			}
		};
	}

	public static IAnnotationOperation hasIntegerValue(@Nonnull String name,
			Integer value) {
		return new EnsureAnnotationField<Integer>(name, IntegerValue.class,
				value) {
			@Override
			public String format(Integer value) {
				return value != null ? Integer.toString(value) : NULL;
			}
		};
	}

	public static IAnnotationOperation hasCharValue(@Nonnull String name,
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

		public IClassOperation withAccess(@Nonnull AccessModifier modifier) {
			return new EnsureField(name, type, modifier);
		}
	}

	public static IFieldOperation hasInitialization(@Nonnull String expression) {
		return new EnsureFieldInitialization(expression);
	}

	public static IMethodOperation hasMethodAnnotation(
			@Nonnull String annotation) {
		return new EnsureMethodAnnotation(annotation);
	}

	public static IParameterOperation hasParameterAnnotation(
			@Nonnull String annotation) {
		return new EnsureParameterAnnotation(annotation);
	}

	public static IBodyContainerOperation hasIfStatement(
			@Nonnull String filterCondition) {
		return new HasIfStatementOperation(filterCondition);
	}

	public static ICompilationUnitOperation hasPackageClass(
			@Nonnull String packageClassName) {
		return new EnsurePackageClass(packageClassName);
	}

	public static IClassOperation hasSuperclass(@Nonnull String superClass) {
		return new EnsureSuperClass(superClass);
	}

	public static IInterfaceOperation extendsInterface(
			@Nonnull String interfaceName) {
		return new EnsureSuperInterface(interfaceName);
	}

	public static IClassOperation implementsInterface(
			@Nonnull String interfaceName) {
		return new EnsureImplements(interfaceName);
	}

	public static <S extends AnalyzedStatement> IJavaOperation<S> hasPrefixComment(
			@Nonnull String comment) {
		return new EnsureStatementComment<S>(comment, true);
	}

	public static <S extends AnalyzedStatement> IJavaOperation<S> hasSuffixComment(
			@Nonnull String comment) {
		return new EnsureStatementComment<S>(comment, false);
	}

}
