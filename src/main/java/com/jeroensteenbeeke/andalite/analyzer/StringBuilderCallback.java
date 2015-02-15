/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.analyzer;

public class StringBuilderCallback implements IOutputCallback {
	private final StringBuilder builder;

	private int indentation = 0;

	public StringBuilderCallback(StringBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public void increaseIndentationLevel() {
		indentation++;
	}

	@Override
	public void write(String data) {
		builder.append(data);
	}

	@Override
	public void newline() {
		builder.append(System.getProperty("line.separator"));
		for (int i = 0; i < indentation; i++) {
			builder.append("\t");
		}
	}

	@Override
	public void decreaseIndentationLevel() {
		indentation--;
	}
}
