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

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnumConstant;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumConstantOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;

import java.util.List;

public class EnsureEnumConstantMethod extends AbstractEnsureMethod<AnalyzedEnumConstant, AnalyzedEnumConstant.EnumConstantInsertionPoint> implements IEnumConstantOperation {
	public EnsureEnumConstantMethod(String name, String type, AccessModifier modifier, List<ParameterDescriptor> descriptors) {
		super(name, type, modifier, descriptors);
	}

	@Override
	protected AnalyzedEnumConstant.EnumConstantInsertionPoint getInsertionPoint() {
		return AnalyzedEnumConstant.EnumConstantInsertionPoint.AFTER_LAST_MEMBER;
	}
}
