package com.jeroensteenbeeke.andalite.analyzer;

import javax.annotation.Nonnull;

import com.github.antlrjavaparser.api.body.ModifierSet;
import com.jeroensteenbeeke.andalite.Location;

public abstract class AccessModifiable extends Annotatable {
	private final AccessModifier modifier;

	private final boolean modifierStatic;

	private final boolean modifierFinal;

	private final boolean modifierAbstract;

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

	@Override
	public final void onOutput(OutputCallback callback) {
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

	public abstract void onModifierOutputted(OutputCallback callback);

}
