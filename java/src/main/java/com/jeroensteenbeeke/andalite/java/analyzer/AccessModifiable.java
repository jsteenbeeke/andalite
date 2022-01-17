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

import com.github.javaparser.ast.Modifier;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class AccessModifiable<T extends AccessModifiable<T, I>, I extends Enum<I> & IInsertionPoint<T>> extends Annotatable<T, I> {
	private final AccessModifier modifier;

	private final boolean modifierStatic;

	private final boolean modifierFinal;

	private boolean modifierAbstract;

	private final boolean modifierSynchronized;

	private final boolean modifierStrictFp;

	private final boolean modifierNative;

	private final boolean modifierVolatile;

	private final boolean modifierTransient;

	public AccessModifiable(@NotNull Location location, List<Modifier.Keyword> modifiers) {
		super(location);
		this.modifier = AccessModifier.fromModifiers(modifiers);
		this.modifierFinal = modifiers.contains(Modifier.Keyword.FINAL);
		this.modifierStatic = modifiers.contains(Modifier.Keyword.STATIC);
		this.modifierAbstract = modifiers.contains(Modifier.Keyword.ABSTRACT);
		this.modifierSynchronized = modifiers.contains(Modifier.Keyword.SYNCHRONIZED);
		this.modifierStrictFp = modifiers.contains(Modifier.Keyword.STRICTFP);
		this.modifierNative = modifiers.contains(Modifier.Keyword.NATIVE);
		this.modifierVolatile = modifiers.contains(Modifier.Keyword.VOLATILE);
		this.modifierTransient = modifiers.contains(Modifier.Keyword.TRANSIENT);
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
