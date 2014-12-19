package com.jeroensteenbeeke.andalite.analyzer;

import com.jeroensteenbeeke.andalite.Location;

public class AnalyzedEnumConstant extends ContainingDenomination {

	public AnalyzedEnumConstant(Location location, int modifiers,
			String packageName, String denominationName) {
		super(location, modifiers, packageName, denominationName);
	}

	@Override
	public void onModifierOutputted(OutputCallback callback) {
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
