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
package com.jeroensteenbeeke.andalite.transformation;

import java.io.Serializable;

import com.google.common.base.Function;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedParameter;

public class ParameterDescriptor {
	private final String type;

	private final String name;

	public ParameterDescriptor(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterDescriptor other = (ParameterDescriptor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public boolean appliesTo(AnalyzedParameter parameter) {
		return name.equals(parameter.getName())
				&& type.equals(parameter.getType());
	}

	@Override
	public String toString() {
		return String.format("%s %s", type, name);
	}

	public static Function<ParameterDescriptor, String> getTypeFunction() {
		return new GetTypeFunction();
	}

	public static class GetTypeFunction implements
			Function<ParameterDescriptor, String>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String apply(ParameterDescriptor input) {
			return input.getType();
		}
	}
}
