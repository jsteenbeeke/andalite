package com.jeroensteenbeeke.andalite.analyzer;

public interface TestInterface {
	void a();

	default void b() {
		c();
	}

	private void c() {
		d();
	}

	private static void d() {

	}

	static void e() {

	}
}
