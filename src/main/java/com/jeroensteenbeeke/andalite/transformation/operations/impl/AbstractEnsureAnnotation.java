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
package com.jeroensteenbeeke.andalite.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.analyzer.Annotatable;
import com.jeroensteenbeeke.andalite.transformation.Transformation;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;
import com.jeroensteenbeeke.andalite.transformation.operations.OperationException;

public abstract class AbstractEnsureAnnotation<T extends Annotatable>
		implements Operation<T> {
	private final String type;

	public AbstractEnsureAnnotation(String type) {
		super();
		this.type = type;
	}

	@Override
	public final List<Transformation> perform(T input)
			throws OperationException {
		if (!input.hasAnnotation(type)) {
			return ImmutableList.of(Transformation.insertBefore(input,
					String.format("\n%s@%s", getPrefix(), type)));
		}

		return ImmutableList.of();
	}

	protected String getPrefix() {
		return "";
	}
}