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

package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.github.javaparser.ast.Modifier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.*;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BaseStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;

public final class AnalyzedConstructor extends AccessModifiable<AnalyzedConstructor, AnalyzedConstructor.ConstructorInsertionPoint> implements
	IBodyContainer<AnalyzedConstructor, AnalyzedConstructor.ConstructorInsertionPoint>, IParameterized {
	private final List<AnalyzedParameter> parameters;

	private final List<AnalyzedStatement<?,?>> statements;

	private final String className;

	private final Location rightParenthesisLocation;

	private Location bodyLocation;

	public AnalyzedConstructor(@Nonnull Location location,
							   @Nonnull String className, @Nonnull List<Modifier.Keyword> modifiers,
							   @Nonnull Location rightParenthesisLocation) {
		super(location, modifiers);

		this.className = className;
		this.parameters = Lists.newArrayList();
		this.statements = Lists.newArrayList();
		this.rightParenthesisLocation = rightParenthesisLocation;


	}

	@Override
	@Nonnull
	public final List<AnalyzedStatement<?,?>> getStatements() {
		return statements;
	}

	void addStatement(AnalyzedStatement<?,?> statement) {
		this.statements.add(statement);
	}

	@Override
	public void addParameter(@Nonnull AnalyzedParameter analyzedParameter) {
		this.parameters.add(analyzedParameter);
	}

	@Nonnull
	@Override
	public List<AnalyzedParameter> getParameters() {
		return ImmutableList.copyOf(parameters);
	}

	public Location getRightParenthesisLocation() {
		return rightParenthesisLocation;
	}

	@Nonnull
	public Optional<Location> getBodyLocation() {
		return Optional.ofNullable(bodyLocation);
	}

	public void setBodyLocation(@Nonnull Location bodyLocation) {
		this.bodyLocation = bodyLocation;
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

	@Override
	public ConstructorInsertionPoint getAnnotationInsertPoint() {
		return ConstructorInsertionPoint.BEFORE;
	}

	@Nonnull
	@Override
	public Transformation insertAt(@Nonnull ConstructorInsertionPoint insertionPoint, @Nonnull String replacement) {
		if (ConstructorInsertionPoint.END_OF_BODY == insertionPoint && !statements.isEmpty()) {
			AnalyzedStatement<?,?> lastStatement = statements.get(statements.size() - 1);
			if (lastStatement instanceof ReturnStatement) {
				ReturnStatement returnStatement = (ReturnStatement) lastStatement;

				return returnStatement.insertAt(BaseStatement.BaseStatementInsertionPoint.BEFORE, replacement);
			}
		}

		return super.insertAt(insertionPoint, replacement);
	}

	@Override
	public ConstructorInsertionPoint getStatementInsertionPoint() {
		return ConstructorInsertionPoint.END_OF_BODY;
	}


	public enum ConstructorInsertionPoint implements IInsertionPoint<AnalyzedConstructor> {
		BEFORE {
			@Override
			public int position(AnalyzedConstructor container) {
				return container.getLocation().getStart();
			}
		},
		AFTER_LAST_PARAMETER {
			@Override
			public int position(AnalyzedConstructor container) {
				return Optional.ofNullable(container.getRightParenthesisLocation())
							   .map(Location::getStart)
							   .orElseThrow(() -> new IllegalStateException("Method without right parenthesis location"));
			}
		},
		START_OF_BODY {
			@Override
			public int position(AnalyzedConstructor container) {
				return container.getBodyLocation().map(Location::getStart)
								.map(s -> s + 1)
								.orElseThrow(() -> new IllegalStateException("Cannot insert into method without body location"));
			}

		},
		END_OF_BODY {
			@Override
			public int position(AnalyzedConstructor container) {
				return container.getBodyLocation().map(Location::getEnd)
								.orElseThrow(() -> new IllegalStateException("Cannot insert into method without body location"));
			}
		}
	}
}
