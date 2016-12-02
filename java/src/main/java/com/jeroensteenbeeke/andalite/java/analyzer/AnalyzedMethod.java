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
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of a Java method
 * 
 * @author Jeroen Steenbeeke
 */
public final class AnalyzedMethod extends AccessModifiable
		implements IBodyContainer, Commentable, IJavadocable, IParameterized {
	private final String name;

	private final AnalyzedType returnType;

	private final List<AnalyzedParameter> parameters;

	private final List<AnalyzedStatement> statements;

	private final List<String> comments;

	private final List<AnalyzedThrownException> thrownExceptions;

	private String javadoc;

	private Location rightParenthesisLocation;

	/**
	 * Create a new AnalyzedMethod
	 * 
	 * @param location
	 *            The location of the method
	 * @param returnType
	 *            The return type of the method. Should equal TypeVoid if it has
	 *            no return type
	 * @param modifiers
	 *            The modifiers of the method, indicating what keywords it has
	 * @param name
	 *            The name of the method
	 */
	AnalyzedMethod(@Nonnull Location location, @Nonnull AnalyzedType returnType,
			int modifiers, @Nonnull String name) {
		super(location, modifiers);
		this.name = name;
		this.returnType = returnType;
		this.parameters = Lists.newArrayList();
		this.statements = Lists.newArrayList();
		this.comments = Lists.newArrayList();
		this.thrownExceptions = Lists.newArrayList();
	}

	/**
	 * Get the location of the right parenthesis of the method (the one after
	 * parameters
	 * 
	 * @return The location of the right parenthesis, if set. {@code null}
	 *         otherwise
	 */
	@CheckForNull
	public Location getRightParenthesisLocation() {
		return rightParenthesisLocation;
	}

	/**
	 * Set the location of the right parenthesis, that is located after method
	 * parameters
	 * 
	 * @param rightParenthesisLocation
	 *            The location of the right parenthesis
	 */
	void setRightParenthesisLocation(
			@Nullable Location rightParenthesisLocation) {
		this.rightParenthesisLocation = rightParenthesisLocation;
	}

	/**
	 * Add an exception to the list of thrown exceptions
	 * 
	 * @param exception
	 *            The exception that might be thrown
	 */
	void addThrownException(@Nonnull AnalyzedThrownException exception) {
		this.thrownExceptions.add(exception);
	}

	/**
	 * Get the list of thrown exceptions
	 * 
	 * @return An immutable list of thrown exceptions
	 */
	@Nonnull
	public List<AnalyzedThrownException> getThrownExceptions() {
		return ImmutableList.copyOf(thrownExceptions);
	}

	@Override
	public void addComment(@Nonnull String comment) {
		comments.add(comment);
	}

	@Override
	public List<String> getComments() {
		return ImmutableList.copyOf(comments);
	}

	/**
	 * Get the name of the method
	 * 
	 * @return The name of the method
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * Get the return type of this method
	 * 
	 * @return The return type of the method. If the method has no return type,
	 *         this is an instance of TypeVoid
	 */
	@Nonnull
	public AnalyzedType getReturnType() {
		return returnType;
	}

	/**
	 * Add a parameter to the method
	 * 
	 * @param analyzedParameter
	 *            The parameter to add
	 */
	void addParameter(@Nonnull AnalyzedParameter analyzedParameter) {
		this.parameters.add(analyzedParameter);
	}

	@Nonnull
	@Override
	public List<AnalyzedParameter> getParameters() {
		return ImmutableList.copyOf(parameters);
	}

	@Override
	@Nonnull
	public final List<AnalyzedStatement> getStatements() {
		return ImmutableList.copyOf(statements);
	}

	/**
	 * Add a statement to the list of statements
	 * 
	 * @param statement
	 *            The statement to add
	 */
	void addStatement(@Nonnull AnalyzedStatement statement) {
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
	public String getJavadoc() {
		return javadoc;
	}

	@Override
	public void setJavadoc(@Nonnull String javadoc) {
		this.javadoc = javadoc;
	}
}
