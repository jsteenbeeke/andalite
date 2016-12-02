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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of a Java Enum
 * 
 * @author Jeroen Steenbeeke
 */
public class AnalyzedEnum extends ConstructableDenomination {
	private final List<AnalyzedEnumConstant> constants;

	private Location separatorLocation;

	/**
	 * Create a new AnalyzedEnum
	 * 
	 * @param location
	 *            The location of the enum definition
	 * @param modifiers
	 *            The modifiers of the class, indicating what keywords it has
	 * @param packageName
	 *            The name of the package the enum is in
	 * @param enumName
	 *            The node containing the name of the enum
	 */
	AnalyzedEnum(@Nonnull Location location, int modifiers,
			@Nonnull String packageName, @Nonnull TerminalNode enumName) {
		super(location, modifiers, packageName, enumName);
		this.constants = Lists.newArrayList();
	}

	/**
	 * Get the name of the enum
	 * 
	 * @return The name of the enum
	 */
	@Nonnull
	public String getEnumName() {
		return getDenominationName();
	}

	/**
	 * Set the location of the semicolon that separates the enum constants from
	 * the generic class body
	 * 
	 * @param separatorLocation
	 *            The location of the semicolon
	 */
	void setSeparatorLocation(@Nonnull Location separatorLocation) {
		this.separatorLocation = separatorLocation;
	}

	/**
	 * Get the location of the semicolon that separates enum constants from the
	 * generic class body
	 * 
	 * @return The location of the semicolon, if it exists, or {@code null}
	 *         otherwise
	 */
	@CheckForNull
	public Location getSeparatorLocation() {
		return separatorLocation;
	}

	/**
	 * Add an enum constant to this enum definition
	 * 
	 * @param constant
	 *            The enum constant to add
	 */
	void addConstant(@Nonnull AnalyzedEnumConstant constant) {
		constants.add(constant);
	}

	/**
	 * Get all enum constants defined for this enum
	 * 
	 * @return An immutable list of enum constants
	 */
	@Nonnull
	public List<AnalyzedEnumConstant> getConstants() {
		return ImmutableList.copyOf(constants);
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("enum ");
		callback.write(getEnumName());
		outputInterfaces(callback);
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		for (AnalyzedField analyzedField : getFields()) {
			analyzedField.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedConstructor constructor : getConstructors()) {
			constructor.output(callback);
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
