package com.jeroensteenbeeke.andalite.forge.util;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;

public class JUnitFeedbackHandler implements FeedbackHandler {
	private final List<String> errors;

	private final List<String> warnings;

	private final List<String> infos;

	private JUnitFeedbackHandler() {
		this.errors = Lists.newArrayList();
		this.warnings = Lists.newArrayList();
		this.infos = Lists.newArrayList();
	}

	@Override
	public void error(String messsage, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warning(String message, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void info(String message, Object... args) {
		// TODO Auto-generated method stub

	}

}
