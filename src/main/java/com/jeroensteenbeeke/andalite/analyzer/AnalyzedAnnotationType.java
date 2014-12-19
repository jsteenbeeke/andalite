package com.jeroensteenbeeke.andalite.analyzer;

import com.jeroensteenbeeke.andalite.Location;

public class AnalyzedAnnotationType extends Denomination {

	protected AnalyzedAnnotationType(Location location, int modifiers,
			String packageName, String denominationName) {
		super(location, modifiers, packageName, denominationName);
	}

	public String getAnnotationName() {
		return getDenominationName();
	}

	@Override
	public void onModifierOutputted(OutputCallback callback) {
		callback.write("@interface ");
		callback.write(getAnnotationName());
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();

		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
		callback.newline();

	}

}
