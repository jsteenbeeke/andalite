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
package com.jeroensteenbeeke.andalite.xml.operations;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import com.jeroensteenbeeke.andalite.xml.IElementOperation;

public class EnsureAttribute implements IElementOperation {
	public static class Builder {
		private final String attributeName;

		public Builder(String attributeName) {
			super();
			this.attributeName = attributeName;
		}

		public EnsureAttribute withValue(String value) {
			return new EnsureAttribute(attributeName, value);
		}
	}

	private final String attributeName;

	private final String value;

	private EnsureAttribute(String attributeName, String value) {
		super();
		this.attributeName = attributeName;
		this.value = value;
	}

	@Override
	public void transform(Element node) {
		node.setAttribute(attributeName, value);
	}

	@Override
	@NotNull
	public String getDescription() {
		return String.format("Ensure that attribute %s is %s", attributeName,
				value);
	}

}
