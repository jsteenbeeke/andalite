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

import com.github.javaparser.ast.Modifier;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;

import java.util.List;
import java.util.stream.Collectors;

public class VariableDeclarationExpression extends AnalyzedExpression {
	private final boolean declaredFinal;

	private final AnalyzedType type;

	private final List<AnalyzedAnnotation> annotations;

	private final List<DeclareVariableExpression> variables;

	public VariableDeclarationExpression(Location location, List<Modifier.Keyword> modifiers,
			AnalyzedType type) {
		super(location);
		this.declaredFinal = modifiers.contains(Modifier.Keyword.FINAL);
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
			Joiner.on(" ").appendTo(
					sb,
					annotations.stream().map(AnalyzedAnnotation::toJavaString).collect(Collectors.toList()));
			sb.append(" ");
		}

		if (isDeclaredFinal()) {
			sb.append("final ");
		}

		sb.append(type.toJavaString());

		if (!variables.isEmpty()) {
			sb.append(" ");
			Joiner.on(", ").appendTo(
					sb,
					variables.stream().map(AnalyzedExpression::toJavaString).collect(Collectors.toList()));
		}

		return sb.toString();
	}
}
