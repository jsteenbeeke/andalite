package com.jeroensteenbeeke.andalite.analyzer;

public enum TestEnum {
	A {
		@Override
		protected void baz() {

		}
	}, B("Foo") {
		@Override
		protected void baz() {

		}
	}, C(0) {
		@Override
		protected void baz() {

		}
	};

	TestEnum() {

	}

	TestEnum(String txt) {

	}

	TestEnum(int val) {

	}

	protected void bar() {

	}

	protected abstract void baz();
}
