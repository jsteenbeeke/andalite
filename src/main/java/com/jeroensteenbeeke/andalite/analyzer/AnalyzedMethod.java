package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedMethod extends AccessModifiable {
	private final String name;

	private String returnType;

	private final List<AnalyzedParameter> parameters;

	public AnalyzedMethod(@Nonnull Location location, int modifiers,
			@Nonnull String name) {
		super(location, modifiers);
		this.name = name;
		this.returnType = "void";
		this.parameters = Lists.newArrayList();
	}

	@Nonnull
	public String getName() {
		return name;
	}

	void addParameter(@Nonnull AnalyzedParameter analyzedParameter) {
		this.parameters.add(analyzedParameter);
	}

	@Nonnull
	public List<AnalyzedParameter> getParameters() {
		return ImmutableList.copyOf(parameters);
	}

	@Override
	public void onModifierOutputted(OutputCallback callback) {
		callback.write(returnType);
		callback.write(" ");
		callback.write(name);
		callback.write("(");
		for (AnalyzedParameter analyzedParameter : getParameters()) {
			analyzedParameter.output(callback);
		}

		callback.write(") {");
		callback.increaseIndentationLevel();
		callback.newline();
		// TODO: body
		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
	}
}
