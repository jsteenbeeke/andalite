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

import org.antlr.v4.runtime.tree.TerminalNode;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of an enum constant
 * 
 * @author Jeroen Steenbeeke
 */
public class AnalyzedEnumConstant extends ContainingDenomination {
	private final List<AnalyzedExpression> parameters;

	/**
	 * Create a new AnalyzedEnumConstant
	 * 
	 * @param location
	 *            The location of the constant
	 * @param modifiers
	 *            The modifiers of the constant, indicating what keywords it
	 *            has. Pretty much unused as enum constants generally don't have
	 *            modifiers
	 * @param packageName
	 *            The name of the package the parent enum is in
	 * @param constantName
	 *            The node containing the name of the enum constant
	 * @param parameters
	 *            The parameters passed to the constant declaration
	 */
	AnalyzedEnumConstant(@Nonnull Location location, int modifiers,
			@Nonnull String packageName, @Nonnull TerminalNode constantName,
			@Nonnull List<AnalyzedExpression> parameters) {
		super(location, modifiers, packageName, constantName);
		this.parameters = parameters;
	}

	/**
	 * Get the list of the parameters passed to the constant declaration
	 * 
	 * @return An immutable list of parameter expression
	 */
	@Nonnull
	public List<AnalyzedExpression> getParameters() {
		return ImmutableList.copyOf(parameters);
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		outputInterfaces(callback);
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		for (AnalyzedField analyzedField : getFields()) {
			analyzedField.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedMethod analyzedMethod : getMethods()) {
			analyzedMethod.output(callback);
			callback.newline();
			callback.newline();
		}

		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
		callback.newline();

	}

}
