package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;

public class AnalyzedEnum extends ConstructableDenomination {
	private final List<AnalyzedEnumConstant> constants;

	public AnalyzedEnum(Location location, int modifiers, String packageName,
			String denominationName) {
		super(location, modifiers, packageName, denominationName);
		this.constants = Lists.newArrayList();
	}

	public String getEnumName() {
		return getDenominationName();
	}

	@Override
	public void onModifierOutputted(OutputCallback callback) {
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

	void addConstant(AnalyzedEnumConstant constant) {
		constants.add(constant);
	}

	public List<AnalyzedEnumConstant> getConstants() {
		return ImmutableList.copyOf(constants);
	}

}
