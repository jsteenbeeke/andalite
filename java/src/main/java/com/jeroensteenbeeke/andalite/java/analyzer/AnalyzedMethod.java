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
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.javaparser.ast.Modifier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.*;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.BaseStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.statements.ReturnStatement;

public final class AnalyzedMethod extends AccessModifiable<AnalyzedMethod, AnalyzedMethod.MethodInsertionPoint>
	implements IBodyContainer<AnalyzedMethod, AnalyzedMethod.MethodInsertionPoint>, Commentable, Javadocable, IParameterized {
	private final String name;

	private final AnalyzedType returnType;

	private final List<AnalyzedParameter> parameters;

	private final List<AnalyzedStatement<?,?>> statements;

	private final List<String> comments;

	private final List<AnalyzedThrownException> thrownExceptions;

	private String javadoc;

	private Location rightParenthesisLocation;

	private Location bodyLocation;

	public AnalyzedMethod(@Nonnull Location location,
						  @Nonnull AnalyzedType returnType, List<Modifier.Keyword> modifiers,
						  @Nonnull String name) {
		super(location, modifiers);
		this.name = name;
		this.returnType = returnType;
		this.parameters = Lists.newArrayList();
		this.statements = Lists.newArrayList();
		this.comments = Lists.newArrayList();
		this.thrownExceptions = Lists.newArrayList();
	}

	@CheckForNull
	public Location getRightParenthesisLocation() {
		return rightParenthesisLocation;
	}

	public void setRightParenthesisLocation(
		@Nullable Location rightParenthesisLocation) {
		this.rightParenthesisLocation = rightParenthesisLocation;
	}

	@Nonnull
	public Optional<Location> getBodyLocation() {
		return Optional.ofNullable(bodyLocation);
	}

	public void setBodyLocation(@Nonnull Location bodyLocation) {
		this.bodyLocation = bodyLocation;
	}

	public void addThrownException(@Nonnull AnalyzedThrownException exception) {
		this.thrownExceptions.add(exception);
	}

	@Nonnull
	public List<AnalyzedThrownException> getThrownExceptions() {
		return thrownExceptions;
	}

	@Override
	public void addComment(@Nonnull String comment) {
		comments.add(comment);
	}

	@Override
	public List<String> getComments() {
		return comments;
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public AnalyzedType getReturnType() {
		return returnType;
	}

	public void addParameter(@Nonnull AnalyzedParameter analyzedParameter) {
		this.parameters.add(analyzedParameter);
	}

	@Nonnull
	@Override
	public List<AnalyzedParameter> getParameters() {
		return ImmutableList.copyOf(parameters);
	}

	@Override
	@Nonnull
	public final List<AnalyzedStatement<?,?>> getStatements() {
		return statements;
	}

	void addStatement(AnalyzedStatement statement) {
		this.statements.add(statement);
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		returnType.output(callback);
		callback.write(" ");
		callback.write(name);
		callback.write("(");
		for (AnalyzedParameter analyzedParameter : getParameters()) {
			analyzedParameter.output(callback);
		}

		callback.write(")");
		if (!getThrownExceptions().isEmpty()) {
			callback.write(" throws ");
			callback.write(getThrownExceptions().stream()
												.map(AnalyzedThrownException::getException)
												.collect(Collectors.joining(", ")));

		}
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		// TODO: body
		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
	}

	@Override
	public String toString() {

		return String.format("%s %s %s (%s)", getAccessModifier().getOutput(),
							 returnType.toJavaString(), name,
							 getParameters().stream().map(AnalyzedParameter::getType)
											.collect(Collectors.joining(", ")));
	}

	@Override
	public Optional<String> getJavadoc() {
		return Optional.ofNullable(javadoc);
	}

	@Override
	public void setJavadoc(String javadoc) {
		this.javadoc = javadoc;
	}

	@Override
	public MethodInsertionPoint getAnnotationInsertPoint() {
		return MethodInsertionPoint.BEFORE;
	}

	@Nonnull
	@Override
	public Transformation insertAt(@Nonnull MethodInsertionPoint insertionPoint, @Nonnull String replacement) {
		if (MethodInsertionPoint.END_OF_BODY == insertionPoint && !statements.isEmpty()) {
			AnalyzedStatement<?,?> lastStatement = statements.get(statements.size() - 1);
			if (lastStatement instanceof ReturnStatement) {
				ReturnStatement returnStatement = (ReturnStatement) lastStatement;

				return returnStatement.insertAt(BaseStatement.BaseStatementInsertionPoint.BEFORE, replacement);
			}
		}

		return super.insertAt(insertionPoint, replacement);
	}

	@Override
	public MethodInsertionPoint getStatementInsertionPoint() {
		return MethodInsertionPoint.END_OF_BODY;
	}

	public enum MethodInsertionPoint implements IInsertionPoint<AnalyzedMethod> {
		BEFORE {
			@Override
			public int position(AnalyzedMethod container) {
				return container.getLocation().getStart();
			}
		},
		AFTER_LAST_PARAMETER {
			@Override
			public int position(AnalyzedMethod container) {
				return Optional.ofNullable(container.getRightParenthesisLocation())
							   .map(Location::getStart)
							   .orElseThrow(() -> new IllegalStateException("Method without right parenthesis location"));
			}
		},
		START_OF_BODY {
			@Override
			public int position(AnalyzedMethod container) {
				return container.getBodyLocation().map(Location::getStart)
								.map(s -> s + 1)
								.orElseThrow(() -> new IllegalStateException("Cannot insert into method without body location"));
			}

		},
		BEFORE_RETURN_TYPE {
			@Override
			public int position(AnalyzedMethod container) {
				return container.getReturnType().getLocation().getStart();
			}
		}, END_OF_BODY {
			@Override
			public int position(AnalyzedMethod container) {
				return container.getBodyLocation().map(Location::getEnd)
								.orElseThrow(() -> new IllegalStateException("Cannot insert into method without body location"));
			}
		}, AFTER_LAST_EXCEPTION {
			@Override
			public int position(AnalyzedMethod container) {
				return container.getBodyLocation().map(Location::getStart)
								.orElseGet(() -> container.getLocation().getEnd() - 1);
			}
		}
	}
}
