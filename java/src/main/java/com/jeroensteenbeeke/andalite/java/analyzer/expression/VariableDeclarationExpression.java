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
package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import java.util.List;
import java.util.stream.Collectors;

import com.github.antlrjavaparser.api.body.ModifierSet;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;

public class VariableDeclarationExpression extends AnalyzedExpression {
	private final boolean declaredFinal;

	private final AnalyzedType type;

	private final List<AnalyzedAnnotation> annotations;

	private final List<DeclareVariableExpression> variables;

	public VariableDeclarationExpression(Location location, int modifiers,
			AnalyzedType type) {
		super(location);
		this.declaredFinal = ModifierSet.isFinal(modifiers);
		this.type = type;
		this.annotations = Lists.newArrayList();
		this.variables = Lists.newArrayList();
	}

	public void addDeclareVariable(DeclareVariableExpression expression) {
		this.variables.add(expression);
	}

	public void addAnnotation(AnalyzedAnnotation annotation) {
		this.annotations.add(annotation);
	}

	public List<AnalyzedAnnotation> getAnnotations() {
		return annotations;
	}

	public List<DeclareVariableExpression> getVariables() {
		return variables;
	}

	public boolean isDeclaredFinal() {
		return declaredFinal;
	}

	public AnalyzedType getType() {
		return type;
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();

		if (!annotations.isEmpty()) {
			sb.append(annotations.stream().map(AnalyzedAnnotation::toJavaString)
					.collect(Collectors.joining(" ", "", " ")));
		}

		if (isDeclaredFinal()) {
			sb.append("final ");
		}

		sb.append(type.toJavaString());

		if (!variables.isEmpty()) {
			sb.append(" ");
			sb.append(variables.stream().map(AnalyzedExpression::toJavaString)
					.collect(Collectors.joining(", ")));
		}

		return sb.toString();
	}
}
