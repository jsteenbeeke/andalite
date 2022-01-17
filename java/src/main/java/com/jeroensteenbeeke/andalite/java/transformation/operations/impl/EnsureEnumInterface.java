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

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.GenerifiedName;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IEnumOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IInterfaceOperation;
import com.jeroensteenbeeke.lux.ActionResult;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class EnsureEnumInterface implements IEnumOperation {
	private final String interfaceName;

	public EnsureEnumInterface(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedEnum input)
		throws OperationException {
		if (input.getInterfaces().stream().map(GenerifiedName::getName).anyMatch(interfaceName::equals)) {
			return ImmutableList.of();
		}

		String prefix = input.getInterfaces().isEmpty() ? " implements " : ", ";

		return ImmutableList.of(input.insertAt(AnalyzedEnum.EnumInsertionPoint.AFTER_LAST_INTERFACE, prefix.concat(interfaceName)));

	}

	@Override
	public String getDescription() {
		return "Ensure that enum implements ".concat(interfaceName);
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedEnum input) {
		if (input
			.getInterfaces()
			.stream()
			.map(GenerifiedName::getName)
			.map(this::stripWhitespaces)
			.anyMatch(stripWhitespaces(interfaceName)::equals)) {
			return ActionResult.ok();
		}

		return ActionResult
			.error("Enum does not implement %s (observed: %s)", interfaceName, input.getInterfaces().stream().map(GenerifiedName::getName).collect(Collectors
																																						 .joining(", ")));
	}

	private String stripWhitespaces(String s) {
		return s.replaceAll("\\s", "");
	}

}
