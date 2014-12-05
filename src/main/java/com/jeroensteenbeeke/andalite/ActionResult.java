package com.jeroensteenbeeke.andalite;

public class ActionResult {
	private final boolean ok;

	private final String message;

	protected ActionResult(boolean ok, String message) {
		this.ok = ok;
		this.message = message;
	}

	public final boolean isOk() {
		return ok;
	}

	public final String getMessage() {
		return message;
	}

	public static ActionResult ok() {
		return new ActionResult(true, null);
	}

	public static ActionResult error(String message, Object... params) {
		if (params.length == 0) {
			return new ActionResult(false, message);
		} else {
			return new ActionResult(false, String.format(message, params));
		}
	}
}