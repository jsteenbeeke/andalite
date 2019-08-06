package com.jeroensteenbeeke.andalite.analyzer;

public class TestInnerClasses {
	static class A {

	}

	class B {

	}

	public void anonymous() {
		Runnable c = new Runnable() {

			@Override
			public void run() {
				boolean a = true;
				while (a) {
					a = false;
				}

				if (a) {

				} else {

				}
			}
		};

		class D implements Runnable {

			@Override
			public void run() {
				do {

				} while (false);
			}


		}

		new D();

		Runnable e = () -> {};
	}
}
