package com.jeroensteenbeeke.andalite.forge.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;

public class JUnitFeedbackHandler implements FeedbackHandler {
	private static final Logger log = LoggerFactory
			.getLogger(JUnitFeedbackHandler.class);

	private final List<String> errors;

	private final List<String> warnings;

	private final List<String> infos;

	public JUnitFeedbackHandler() {
		this.errors = Lists.newArrayList();
		this.warnings = Lists.newArrayList();
		this.infos = Lists.newArrayList();
	}

	@Override
	public void error(String message, Object... args) {
		String msg = format(message, args);
		log.error(msg);
		process(errors, msg);
	}

	@Override
	public void warning(String message, Object... args) {
		String msg = format(message, args);
		log.warn(msg);
		process(warnings, msg);
	}

	@Override
	public void info(String message, Object... args) {
		String msg = format(message, args);
		log.info(msg);
		process(infos, msg);
	}

	private String format(String message, Object[] args) {
		if (args.length == 0) {
			return message;
		} else {
			return String.format(message, args);
		}
	}

	private void process(List<String> target, String message) {
		target.add(message);
	}

	public boolean hasError(String error) {
		return errors.contains(errors);
	}

	public boolean hasWarning(String warning) {
		return warnings.contains(warning);
	}

	public boolean hasInfo(String info) {
		return infos.contains(info);
	}

	public boolean noError() {
		return errors.isEmpty();
	}

	public boolean noWarning() {
		return warnings.isEmpty();
	}

	public boolean noInfo() {
		return infos.isEmpty();
	}

}
