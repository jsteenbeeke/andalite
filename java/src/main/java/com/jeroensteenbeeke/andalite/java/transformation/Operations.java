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

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.BooleanValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.CharValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.FieldAccessValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.IntegerValue;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.StringValue;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IAnnotationOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IBodyContainerOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IConstructorOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IFieldOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IInterfaceOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IMethodOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IParameterOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IStatementOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.ConstructorParameterAdditionBuilder;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureAnnotationField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureConstructorAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureEndReturnStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureFieldAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureFieldInitialization;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureInnerAnnotationField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureMethodAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureMethodComment;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureMethodFinal;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureMethodJavadoc;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureNextStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureParameterAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureStatementComment;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureSuperInterface;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.HasIfStatementOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.MethodThrowsException;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.RemoveMethodAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.RemoveParameterAnnotation;

@Deprecated
public class Operations {

	private static final String NULL = "null";

	private Operations() {

	}

	public static IBodyContainerOperation returnsAsLastStatement(
			@Nonnull String returnValue) {
		return new EnsureEndReturnStatement(returnValue);
	}

	public static IBodyContainerOperation hasStatement(
			@Nonnull String statement) {
		return new EnsureStatement(statement);
	}

	public static IStatementOperation hasNextStatement(
			@Nonnull String statement) {
		return new EnsureNextStatement(statement);
	}

	public static IFieldOperation hasFieldAnnotation(
			@Nonnull String annotation) {
		return new EnsureFieldAnnotation(annotation);
	}

	public static EnsureEnumMethodBuilder hasEnumMethod() {
		return new EnsureEnumMethodBuilder();
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

	public static IAnnotationOperation hasFieldAccessValue(@Nonnull String name,
			@Nonnull String fieldAccess) {
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
		return new EnsureAnnotationField<String>(name, StringValue.class,
				value) {
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

	public static IFieldOperation hasInitialization(
			@Nonnull String expression) {
		return new EnsureFieldInitialization(expression);
	}

	public static IMethodOperation hasMethodAnnotation(
			@Nonnull String annotation) {
		return new EnsureMethodAnnotation(annotation);
	}

	public static IConstructorOperation hasConstructorAnnotation(
			@Nonnull String annotation) {
		return new EnsureConstructorAnnotation(annotation);
	}

	public static IParameterOperation hasParameterAnnotation(
			@Nonnull String annotation) {
		return new EnsureParameterAnnotation(annotation);
	}

	public static IBodyContainerOperation hasIfStatement(
			@Nonnull String filterCondition) {
		return new HasIfStatementOperation(filterCondition);
	}

	public static IInterfaceOperation extendsInterface(
			@Nonnull String interfaceName) {
		return new EnsureSuperInterface(interfaceName);
	}

	public static <S extends AnalyzedStatement> IJavaOperation<S> hasPrefixComment(
			@Nonnull String comment) {
		return new EnsureStatementComment<S>(comment, true);
	}

	public static <S extends AnalyzedStatement> IJavaOperation<S> hasSuffixComment(
			@Nonnull String comment) {
		return new EnsureStatementComment<S>(comment, false);
	}

	public static HasEnumConstantBuilder hasEnumConstant() {
		return new HasEnumConstantBuilder();
	}

	public static IMethodOperation hasMethodComment(String comment) {
		return new EnsureMethodComment(comment);
	}

	public static IMethodOperation hasMethodJavadoc(String comment) {
		return new EnsureMethodJavadoc(comment);
	}

	public static IMethodOperation ensureMethodFinal() {
		return new EnsureMethodFinal();
	}

	public static ConstructorParameterAdditionBuilder hasConstructorParameterNamed(
			String identifier) {

		return new ConstructorParameterAdditionBuilder(identifier);
	}

	public static IParameterOperation removeParameterAnnotation(
			String annotation) {
		return new RemoveParameterAnnotation(annotation);
	}

	public static IMethodOperation removeMethodAnnotation(String annotation) {
		return new RemoveMethodAnnotation(annotation);
	}

	public static IMethodOperation methodThrowsException(String exception) {
		return new MethodThrowsException(exception);
	}

}
