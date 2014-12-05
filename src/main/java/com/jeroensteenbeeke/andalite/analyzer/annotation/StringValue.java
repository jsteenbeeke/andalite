package com.jeroensteenbeeke.andalite.analyzer.annotation;

import com.jeroensteenbeeke.andalite.analyzer.OutputCallback;

public class StringValue extends BaseValue<String> {

	public StringValue(String name, String value) {
		super(name, value);
	}

	@Override
	public void output(OutputCallback callback) {
		callback.write("\"");
		callback.write(getValue());
		callback.write("\"");
	}

}
