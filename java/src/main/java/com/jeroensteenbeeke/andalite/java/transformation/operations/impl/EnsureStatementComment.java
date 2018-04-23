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
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;

public class EnsureStatementComment<S extends AnalyzedStatement> implements
		IJavaOperation<S> {

	private final String comment;

	private final boolean prefix;

	public EnsureStatementComment(String comment, boolean prefix) {
		super();
		this.comment = comment;
		this.prefix = prefix;
	}

	@Override
	public List<Transformation> perform(S input) throws OperationException {
		if (!input.getComments().contains("// ".concat(comment))) {
			if (prefix) {
				return ImmutableList.of(Transformation.insertBefore(input,
						String.format("// %s\n", comment)));
			} else {
				return ImmutableList.of(Transformation.insertAfter(input,
						String.format("// %s\n", comment)));
			}
		}

		return ImmutableList.of();
	}

	@Override
	public String getDescription() {
		return "Ensure statement has comment: ".concat(comment);
	}

	@Override
	public ActionResult verify(S input) {
		if (!input.getComments().contains("// ".concat(comment))) {
			return ActionResult.error("Missing comment: %s", comment);
		}

		return ActionResult.ok();
	}
}
