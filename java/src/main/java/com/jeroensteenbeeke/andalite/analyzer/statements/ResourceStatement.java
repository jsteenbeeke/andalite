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
package com.jeroensteenbeeke.andalite.analyzer.statements;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class ResourceStatement extends AnalyzedStatement {
	private final AnalyzedType type;

	private final String name;

	private final boolean declaredFinal;

	private final List<AnalyzedAnnotation> annotations;

	private AnalyzedExpression initializer;

	public ResourceStatement(Location location, AnalyzedType type, String name,
			boolean declaredFinal) {
		super(location);
		this.type = type;
		this.name = name;
		this.declaredFinal = declaredFinal;
		this.annotations = Lists.newArrayList();
		this.initializer = null;
	}

	public void addAnnotation(AnalyzedAnnotation annotation) {
		this.annotations.add(annotation);
	}

	public List<AnalyzedAnnotation> getAnnotations() {
		return annotations;
	}

	public AnalyzedExpression getInitializer() {
		return initializer;
	}

	public void setInitializer(AnalyzedExpression initializer) {
		this.initializer = initializer;
	}

	public String getName() {
		return name;
	}

	public AnalyzedType getType() {
		return type;
	}

	public boolean isDeclaredFinal() {
		return declaredFinal;
	}

	@Override
	public String toJavaString() {
		return "";
	}

}
