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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.analyzer.types.ClassOrInterface;

import javax.annotation.CheckForNull;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectCreationExpression extends AnalyzedExpression {
	private final ClassOrInterface type;

	private AnalyzedExpression scope;

	private List<AnalyzedExpression> arguments;

	private List<AnalyzedType> typeArguments;

	private AnalyzedClass declaredAnonymousClass;

	private boolean diamond = false;

	public ObjectCreationExpression(@NotNull Location location,
			@NotNull ClassOrInterface type) {
		super(location);
		this.type = type;
		this.arguments = Lists.newArrayList();
		this.typeArguments = Lists.newArrayList();
	}

	public void setDiamond(boolean diamond) {
		this.diamond = diamond;
	}

	@CheckForNull
	public AnalyzedClass getDeclaredAnonymousClass() {
		return declaredAnonymousClass;
	}

	@NotNull
	public ClassOrInterface getType() {
		return type;
	}

	@CheckForNull
	public AnalyzedExpression getScope() {
		return scope;
	}

	/**
	 * @param scope
	 *            The class scope of the created object
	 * @non.public
	 */
	public void setScope(AnalyzedExpression scope) {
		this.scope = scope;
	}

	@NotNull
	public List<AnalyzedExpression> getArguments() {
		return ImmutableList.copyOf(arguments);
	}

	/**
	 * @param declaredAnonymousClass
	 *            The class to set as anonymous inner class
	 * @non.public
	 */
	public void setDeclaredAnonymousClass(
			@NotNull AnalyzedClass declaredAnonymousClass) {
		this.declaredAnonymousClass = declaredAnonymousClass;
	}

	/**
	 * @param expression
	 *            The expression to add as constructor argument
	 * @non.public
	 */
	public void addArgument(@NotNull AnalyzedExpression expression) {
		arguments.add(expression);
	}

	@NotNull
	public List<AnalyzedType> getTypeArguments() {
		return ImmutableList.copyOf(typeArguments);
	}

	/**
	 * @param type
	 *            The type argument to add to the list
	 * @non.public
	 */
	public void addTypeArgument(@NotNull AnalyzedType type) {
		typeArguments.add(type);
	}

	@Override
	public String toJavaString() {
		StringBuilder java = new StringBuilder();

		java.append("new ");
		if (scope != null) {
			java.append(scope.toJavaString());
			java.append(".");
		}
		java.append(type.toJavaString());
		if (diamond || !typeArguments.isEmpty()) {
			java.append("<");
			Joiner.on(",").appendTo(
					java,
					typeArguments.stream().map(AnalyzedType::toJavaString).collect(Collectors.toList()));
			java.append(">");
		}
		java.append("(");
		if (!arguments.isEmpty()) {
			Joiner.on(",").appendTo(
					java,
					arguments
						.stream()
						.map(AnalyzedExpression::toJavaString)
						.collect(Collectors.toList()));
		}
		java.append(")");

		return java.toString();
	}

}
