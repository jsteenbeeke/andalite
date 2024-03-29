/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.transformation.returntypes.MethodReturnType;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.transformation.ParameterDescriptor;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;
import com.jeroensteenbeeke.andalite.java.util.AnalyzeUtil;
import org.jetbrains.annotations.NotNull;

public class EnsureEnumMethod extends AbstractEnsureMethod<AnalyzedEnum, AnalyzedEnum.EnumInsertionPoint> implements IEnumOperation {
	public EnsureEnumMethod(
		@NotNull String name, @NotNull MethodReturnType type, @NotNull AccessModifier modifier, @NotNull List<ParameterDescriptor> descriptors) {
		super(name, type, modifier, descriptors);
	}

	@Override
	protected AnalyzedEnum.EnumInsertionPoint getInsertionPoint() {
		return AnalyzedEnum.EnumInsertionPoint.END_OF_IMPLEMENTATION;
	}
}
