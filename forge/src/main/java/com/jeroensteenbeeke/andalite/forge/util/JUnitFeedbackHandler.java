package com.jeroensteenbeeke.andalite.forge.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.util.Strings;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;

/**
 * FeedbackHandler implementation for use with JUnit tests
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class JUnitFeedbackHandler implements FeedbackHandler {
	private static final Logger log = LoggerFactory
			.getLogger(JUnitFeedbackHandler.class);

	private final List<String> errors;

	private final List<String> warnings;

	private final List<String> infos;

	/**
	 * Create a new JUnit feedback handler
	 */
	public JUnitFeedbackHandler() {
		this.errors = Lists.newArrayList();
		this.warnings = Lists.newArrayList();
		this.infos = Lists.newArrayList();
	}

	@Override
	public void error(String message, Object... args) {
		String msg = Strings.conditionalFormat(message, args);
		log.error(msg);
		errors.add(msg);
	}

	@Override
	public void warning(String message, Object... args) {
		String msg = Strings.conditionalFormat(message, args);
		log.warn(msg);
		warnings.add(msg);
	}

	@Override
	public void info(String message, Object... args) {
		String msg = Strings.conditionalFormat(message, args);
		log.info(msg);
		infos.add(msg);
	}

	/**
	 * Check if a given error was logged
	 * 
	 * @param error
	 *            The error to check for
	 * @return {@code true} if the error was logged, {@code false} otherwise
	 */
	public boolean hasError(String error) {
		return errors.contains(errors);
	}

	/**
	 * Check if a given warning was logged
	 * 
	 * @param warning
	 *            The warning to check for
	 * @return {@code true} if the warning was logged, {@code false} otherwise
	 */
	public boolean hasWarning(String warning) {
		return warnings.contains(warning);
	}

	/**
	 * Check if a given info message was logged
	 * 
	 * @param info
	 *            The info message to check for
	 * @return {@code true} if the info message was logged, {@code false}
	 *         otherwise
	 */
	public boolean hasInfo(String info) {
		return infos.contains(info);
	}

	/**
	 * Checks if there are no error messages
	 * 
	 * @return {@code true} if there are no error messages
	 */
	public boolean noError() {
		return errors.isEmpty();
	}

	/**
	 * Checks if there are no warning messages
	 * 
	 * @return {@code true} if there are no warning messages
	 */
	public boolean noWarning() {
		return warnings.isEmpty();
	}

	/**
	 * Checks if there are no info messages
	 * 
	 * @return {@code true} if there are no info messages
	 */
	public boolean noInfo() {
		return infos.isEmpty();
	}

}
