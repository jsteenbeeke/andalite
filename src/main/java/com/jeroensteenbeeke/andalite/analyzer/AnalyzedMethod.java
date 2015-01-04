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

package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedMethod extends AccessModifiable implements
		IBodyContainer {
	private final String name;

	private String returnType;

	private final List<AnalyzedParameter> parameters;

	private final List<AnalyzedStatement> statements;

	public AnalyzedMethod(@Nonnull Location location, int modifiers,
			@Nonnull String name) {
		super(location, modifiers);
		this.name = name;
		this.returnType = "void";
		this.parameters = Lists.newArrayList();
		this.statements = Lists.newArrayList();
	}

	@Nonnull
	public String getName() {
		return name;
	}

	public String getReturnType() {
		return returnType;
	}

	void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	void addParameter(@Nonnull AnalyzedParameter analyzedParameter) {
		this.parameters.add(analyzedParameter);
	}

	@Nonnull
	public List<AnalyzedParameter> getParameters() {
		return ImmutableList.copyOf(parameters);
	}

	@Override
	@Nonnull
	public final List<AnalyzedStatement> getStatements() {
		return statements;
	}

	void addStatement(AnalyzedStatement statement) {
		this.statements.add(statement);
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write(returnType);
		callback.write(" ");
		callback.write(name);
		callback.write("(");
		for (AnalyzedParameter analyzedParameter : getParameters()) {
			analyzedParameter.output(callback);
		}

		callback.write(") {");
		callback.increaseIndentationLevel();
		callback.newline();
		// TODO: body
		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
	}
}
