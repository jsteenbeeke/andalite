package com.jeroensteenbeeke.andalite;

public class TypedActionResult<T> extends ActionResult {
	private final T object;

	protected TypedActionResult(boolean ok, String message, T object) {
		super(ok, message);
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	public static final <T> TypedActionResult<T> fail(ActionResult failure) {
		if (failure.isOk())
			throw new IllegalArgumentException(
					"Cannot turn a successful result into a failure");

		return fail(failure.getMessage());

	}

	public static final <T> TypedActionResult<T> ok(T object) {
		return new TypedActionResult<T>(true, null, object);
	}

	public static final <T> TypedActionResult<T> fail(String format,
			Object... params) {
		if (params.length == 0) {
			return new TypedActionResult<T>(false, format, null);
		}

		return new TypedActionResult<T>(false, String.format(format, params),
				null);
	}

}
