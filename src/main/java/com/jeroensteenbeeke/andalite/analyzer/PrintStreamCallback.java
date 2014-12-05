package com.jeroensteenbeeke.andalite.analyzer;

import java.io.PrintStream;

public class PrintStreamCallback implements OutputCallback {
	private final PrintStream stream;

	private int indentation = 0;

	public PrintStreamCallback(PrintStream stream) {
		super();
		this.stream = stream;
	}

	@Override
	public void increaseIndentationLevel() {
		indentation++;
	}

	@Override
	public void write(String data) {
		stream.print(data);
	}

	@Override
	public void newline() {
		stream.println();
		for (int i = 0; i < indentation; i++) {
			stream.print("\t");
		}
	}

	@Override
	public void decreaseIndentationLevel() {
		indentation--;
	}

}
