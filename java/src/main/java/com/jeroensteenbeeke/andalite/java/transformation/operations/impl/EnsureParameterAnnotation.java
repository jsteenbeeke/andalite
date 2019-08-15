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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IParameterOperation;

public class EnsureParameterAnnotation
		extends AbstractEnsureAnnotation<AnalyzedParameter, AnalyzedParameter.ParameterInsertionPoint>
		implements IParameterOperation {

	public EnsureParameterAnnotation(String type) {
		super(type);
	}

	@Override
	protected boolean isNewlineBefore(@Nonnull AnalyzedParameter input) {
		return false;
	}

	@Override
	protected boolean isNewlineAfter(@Nonnull AnalyzedParameter input) {
		return false;
	}

}
