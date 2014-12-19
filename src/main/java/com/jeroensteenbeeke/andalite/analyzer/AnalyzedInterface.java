package com.jeroensteenbeeke.andalite.analyzer;

import com.jeroensteenbeeke.andalite.Location;

public class AnalyzedInterface extends ContainingDenomination {

	public AnalyzedInterface(Location location, int modifiers,
			String packageName, String denominationName) {
		super(location, modifiers, packageName, denominationName);
	}

	public String getInterfaceName() {
		return getDenominationName();
	}

	@Override
	public boolean isAutoAbstractMethods() {
		return true;
	}

	@Override
	public void onModifierOutputted(OutputCallback callback) {
		callback.write("interface ");
		callback.write(getInterfaceName());
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
