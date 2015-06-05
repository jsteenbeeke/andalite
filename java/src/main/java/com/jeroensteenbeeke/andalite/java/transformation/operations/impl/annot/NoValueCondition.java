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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl.annot;

import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;

public class NoValueCondition implements InnerAnnotationCondition {
	private final String name;

	public NoValueCondition(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean isSatisfiedBy(AnnotationValue value) {
		return !value.getValue().hasValueNamed(name);
	}

	@Override
	public String toString() {
		return String.format("Does not have a value '%s'", name);
	}
}