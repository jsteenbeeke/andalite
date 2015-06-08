package com.jeroensteenbeeke.andalite.forge.ui;

public interface FeedbackHandler {

	void error(String messsage, Object... args);

	void warning(String message, Object... args);

	void info(String message, Object... args);

}
