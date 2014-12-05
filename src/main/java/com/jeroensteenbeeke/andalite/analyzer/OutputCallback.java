package com.jeroensteenbeeke.andalite.analyzer;

public interface OutputCallback {
	void increaseIndentationLevel();

	void write(String data);

	void newline();

	void decreaseIndentationLevel();
}
