/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.analyzer.expression;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaExpression extends AnalyzedExpression {
	private final AnalyzedStatement bodyStatement;

	private final List<AnalyzedParameter> parameters;

	public LambdaExpression(@NotNull Location location, @NotNull AnalyzedStatement bodyStatement, @NotNull List<AnalyzedParameter> parameters) {
		super(location);
		this.bodyStatement = bodyStatement;
		this.parameters = parameters;
	}

	@NotNull
	public AnalyzedStatement getBodyStatement() {
		return bodyStatement;
	}

	@NotNull
	public List<AnalyzedParameter> getParameters() {
		return parameters;
	}

	@Override
	public String toJavaString() {
		return String.format("(%2$s) -> %1$s", bodyStatement.toJavaString(), parameters
			.stream()
			.map(AnalyzedParameter::toJavaString)
			.collect(Collectors
						 .joining(", ")));
	}

}
