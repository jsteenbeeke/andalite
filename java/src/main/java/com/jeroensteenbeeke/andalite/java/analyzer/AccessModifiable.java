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

import javax.annotation.Nonnull;

import com.github.antlrjavaparser.api.body.ModifierSet;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Java language element that can have access- and other modifiers
 * 
 * @author Jeroen Steenbeeke
 *
 */
public abstract class AccessModifiable extends Annotatable {
	private final AccessModifier modifier;

	private final boolean modifierStatic;

	private final boolean modifierFinal;

	private boolean modifierAbstract;

	private final boolean modifierSynchronized;

	private final boolean modifierStrictFp;

	private final boolean modifierNative;

	private final boolean modifierVolatile;

	private final boolean modifierTransient;

	/**
	 * Creates a new access modifiable element
	 * 
	 * @param location
	 *            The location in the source file
	 * @param modifiers
	 *            The modifiers that indicate the access of this element.
	 * @see java.lang.reflect.Modifier
	 */
	public AccessModifiable(@Nonnull Location location, int modifiers) {
		super(location);
		this.modifier = AccessModifier.fromModifiers(modifiers);
		this.modifierFinal = ModifierSet.isFinal(modifiers);
		this.modifierStatic = ModifierSet.isStatic(modifiers);
		this.modifierAbstract = ModifierSet.isAbstract(modifiers);
		this.modifierSynchronized = ModifierSet.isSynchronized(modifiers);
		this.modifierStrictFp = ModifierSet.isStrictfp(modifiers);
		this.modifierNative = ModifierSet.isNative(modifiers);
		this.modifierVolatile = ModifierSet.isVolatile(modifiers);
		this.modifierTransient = ModifierSet.isTransient(modifiers);
	}

	/**
	 * Get the access level of this element (public, package/default, protected,
	 * private)
	 * 
	 * @return An AccessModifier variable
	 */
	@Nonnull
	public AccessModifier getAccessModifier() {
		return modifier;
	}

	/**
	 * Determines whether or not the element has the {@code static} modifier
	 * 
	 * @return {@code true} if the element has the {@code static} modifier,
	 *         {@code false} otherwise
	 */
	public final boolean isStatic() {
		return modifierStatic;
	}

	/**
	 * Determines whether or not the element has the {@code final} modifier
	 * 
	 * @return {@code true} if the element has the {@code final} modifier,
	 *         {@code false} otherwise
	 */
	public final boolean isFinal() {
		return modifierFinal;
	}

	/**
	 * Determines whether or not the element has the {@code abstract} modifier
	 * 
	 * @return {@code true} if the element has the {@code abstract} modifier,
	 *         {@code false} otherwise
	 */

	public final boolean isAbstract() {
		return modifierAbstract;
	}

	/**
	 * Determines whether or not the element has the {@code native} modifier
	 * 
	 * @return {@code true} if the element has the {@code native} modifier,
	 *         {@code false} otherwise
	 */

	public final boolean isNative() {
		return modifierNative;
	}

	/**
	 * Determines whether or not the element has the {@code strictfp} modifier
	 * 
	 * @return {@code true} if the element has the {@code strictfp} modifier,
	 *         {@code false} otherwise
	 */
	public final boolean isStrictFp() {
		return modifierStrictFp;
	}

	/**
	 * Determines whether or not the element has the {@code synchronized}
	 * modifier
	 * 
	 * @return {@code true} if the element has the {@code synchronized}
	 *         modifier,
	 *         {@code false} otherwise
	 */
	public final boolean isSynchronized() {
		return modifierSynchronized;
	}

	/**
	 * Determines whether or not the element has the {@code transient} modifier
	 * 
	 * @return {@code true} if the element has the {@code transient} modifier,
	 *         {@code false} otherwise
	 */
	public final boolean isTransient() {
		return modifierTransient;
	}

	/**
	 * Determines whether or not the element has the {@code volatile} modifier
	 * 
	 * @return {@code true} if the element has the {@code volatile} modifier,
	 *         {@code false} otherwise
	 */
	public final boolean isVolatile() {
		return modifierVolatile;
	}

	/**
	 * Modifies the abstract modifier of this element. This is generally needed
	 * for elements that are implicitly abstract, and don't have a modifier
	 * 
	 * @param modifierAbstract
	 *            Whether or not the element is abstract
	 */
	protected final void setAbstract(boolean modifierAbstract) {
		this.modifierAbstract = modifierAbstract;
	}

	@Override
	public final void onOutput(IOutputCallback callback) {
		callback.write(modifier.getOutput());

		if (modifierAbstract) {
			callback.write("abstract ");
		}
		if (modifierStatic) {
			callback.write("static ");
		}
		if (modifierFinal) {
			callback.write("final ");
		}
		if (modifierStrictFp) {
			callback.write("strictfp ");
		}
		if (modifierNative) {
			callback.write("native ");
		}
		if (modifierSynchronized) {
			callback.write("synchronized ");
		}
		if (modifierTransient) {
			callback.write("transient ");
		}
		if (modifierVolatile) {
			callback.write("volatile ");
		}

		onModifierOutputted(callback);
	}

	/**
	 * Continues the output of this element now that modifiers have been
	 * outputted
	 * 
	 * @param callback
	 *            The callback for writing the output
	 */
	public abstract void onModifierOutputted(IOutputCallback callback);

}
