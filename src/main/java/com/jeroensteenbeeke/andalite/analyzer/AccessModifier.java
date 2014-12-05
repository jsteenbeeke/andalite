package com.jeroensteenbeeke.andalite.analyzer;

import com.github.antlrjavaparser.api.body.ModifierSet;

public enum AccessModifier {
	PUBLIC, DEFAULT {
		@Override
		public String getOutput() {
			return "";
		}
	},
	PROTECTED, PRIVATE;

	public String getOutput() {
		return String.format("%s ", name().toLowerCase());
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	public static AccessModifier fromModifiers(int modifiers) {
		if (ModifierSet.isPrivate(modifiers)) {
			return PRIVATE;
		}
		if (ModifierSet.isProtected(modifiers)) {
			return PROTECTED;
		}
		if (ModifierSet.isPublic(modifiers)) {
			return PUBLIC;
		}

		return DEFAULT;
	}
}
