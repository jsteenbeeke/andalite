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

import javax.annotation.Nonnull;

import com.github.antlrjavaparser.api.body.ModifierSet;
import com.jeroensteenbeeke.andalite.Location;

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
		;
	}

	public AccessModifier getAccessModifier() {
		return modifier;
	}

	public boolean isStatic() {
		return modifierStatic;
	}

	public final boolean isFinal() {
		return modifierFinal;
	}

	public boolean isAbstract() {
		return modifierAbstract;
	}

	public boolean isNative() {
		return modifierNative;
	}

	public boolean isStrictFp() {
		return modifierStrictFp;
	}

	public boolean isSynchronized() {
		return modifierSynchronized;
	}

	public boolean isTransient() {
		return modifierTransient;
	}

	public boolean isVolatile() {
		return modifierVolatile;
	}

	protected void setAbstract(boolean modifierAbstract) {
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

	public abstract void onModifierOutputted(IOutputCallback callback);

}
