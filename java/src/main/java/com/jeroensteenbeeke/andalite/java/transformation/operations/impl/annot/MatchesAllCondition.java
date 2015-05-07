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

import java.util.List;

import com.google.common.base.Joiner;
import com.jeroensteenbeeke.andalite.java.analyzer.annotation.AnnotationValue;

public class MatchesAllCondition implements InnerAnnotationCondition {
	private final List<InnerAnnotationCondition> conditions;

	public MatchesAllCondition(List<InnerAnnotationCondition> conditions) {
		super();
		this.conditions = conditions;
	}

	@Override
	public boolean isSatisfiedBy(AnnotationValue value) {
		for (InnerAnnotationCondition condition : conditions) {
			if (!condition.isSatisfiedBy(value)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return String.format("All of {%s}", Joiner.on(", ").join(conditions));
	}

}
