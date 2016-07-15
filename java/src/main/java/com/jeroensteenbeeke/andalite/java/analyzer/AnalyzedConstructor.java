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

package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

public final class AnalyzedConstructor extends AccessModifiable implements
		IBodyContainer, IParameterized {
	private final List<AnalyzedParameter> parameters;

	private final List<AnalyzedStatement> statements;

	private final String className;

	private final Location parametersStartLocation;

	public AnalyzedConstructor(@Nonnull Location location,
			@Nonnull String className, int modifiers,
			@Nonnull Location parametersStartLocation) {
		super(location, modifiers);
		this.className = className;
		this.parameters = Lists.newArrayList();
		this.statements = Lists.newArrayList();
		this.parametersStartLocation = parametersStartLocation;
	}

	@Override
	@Nonnull
	public final List<AnalyzedStatement> getStatements() {
		return statements;
	}

	void addStatement(AnalyzedStatement statement) {
		this.statements.add(statement);
	}

	void addParameter(@Nonnull AnalyzedParameter analyzedParameter) {
		this.parameters.add(analyzedParameter);
	}

	@Nonnull
	@Override
	public List<AnalyzedParameter> getParameters() {
		return ImmutableList.copyOf(parameters);
	}

	public Location getParametersStartLocation() {
		return parametersStartLocation;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write(className);
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
