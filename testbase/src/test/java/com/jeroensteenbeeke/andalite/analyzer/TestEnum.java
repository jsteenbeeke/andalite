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
		void fb() {

		}

		@Override
		protected void baz() {
			fb();
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

	abstract void baz();
}
