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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of a Java class
 * 
 * @author Jeroen Steenbeeke
 *
 */
public final class AnalyzedClass extends ConstructableDenomination {

	private Location extendsLocation = null;

	private String superClass = null;

	AnalyzedClass(@Nonnull Location location, int modifiers,
			@Nonnull String packageName, @Nonnull TerminalNode className) {
		super(location, modifiers, packageName, className);
	}

	/**
	 * Gets the name of this class
	 * 
	 * @return The classname
	 */
	@Nonnull
	public String getClassName() {
		return getDenominationName();
	}

	/**
	 * Gets the location of the extends declaration
	 * 
	 * @return The location of the extends expression, or {@code null} if it
	 *         does not exist
	 */
	@CheckForNull
	public Location getExtendsLocation() {
		return extendsLocation;
	}

	void setExtendsLocation(@Nonnull Location extendsLocation) {
		this.extendsLocation = extendsLocation;
	}

	@CheckForNull
	public String getSuperClass() {
		return superClass;
	}

	void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("class ");
		callback.write(getClassName());
		if (superClass != null) {
			callback.write(" extends ");
			callback.write(superClass);
		}

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
