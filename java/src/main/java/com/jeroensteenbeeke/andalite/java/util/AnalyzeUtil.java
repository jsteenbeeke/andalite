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
package com.jeroensteenbeeke.andalite.java.util;

import java.util.List;

import javax.annotation.Nonnull;

import com.github.antlrjavaparser.api.expr.NameExpr;
import com.github.antlrjavaparser.api.expr.QualifiedNameExpr;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;

public final class AnalyzeUtil {
	private AnalyzeUtil() {

	}

	public static String getQualifiedName(NameExpr expr) {
		if (expr instanceof QualifiedNameExpr) {
			QualifiedNameExpr qualExpr = (QualifiedNameExpr) expr;

			return String.format("%s.%s",
					getQualifiedName(qualExpr.getQualifier()), expr.getName());
		}

		return expr.getName();
	}

	public static boolean matchesSignature(
			@Nonnull AnalyzedMethod analyzedMethod,
			@Nonnull List<ParameterDescriptor> descriptors) {

		List<AnalyzedParameter> parameters = analyzedMethod.getParameters();
		if (parameters.size() == descriptors.size()) {
			int i = 0;

			for (ParameterDescriptor descriptor : descriptors) {
				if (!descriptor.appliesTo(parameters.get(i++))) {
					return false;
				}
			}

			return true;

		}

		return false;
	}

	public static String getMethodSignature(@Nonnull String name,
			@Nonnull List<ParameterDescriptor> descriptors) {
		return String.format(
				"%s(%s)",
				name,
				Joiner.on(", ").join(
						FluentIterable.from(descriptors).transform(
								ParameterDescriptor.getTypeFunction())));
	}
}
