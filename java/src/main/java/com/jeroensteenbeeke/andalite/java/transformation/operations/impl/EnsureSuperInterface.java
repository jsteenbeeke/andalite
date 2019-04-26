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

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.GenerifiedName;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IInterfaceOperation;

public class EnsureSuperInterface implements IInterfaceOperation {
	private final String interfaceName;

	public EnsureSuperInterface(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Override
	public List<Transformation> perform(AnalyzedInterface input)
		throws OperationException {
		if (input.getInterfaces().contains(interfaceName)) {
			return ImmutableList.of();
		}

		return ImmutableList.of(input.insertAt(AnalyzedInterface.InterfaceInsertionPoint.AT_SUPERINTERFACE_POINT, ", ".concat(interfaceName)));

	}

	@Override
	public String getDescription() {
		return "Ensure that class implements ".concat(interfaceName);
	}

	@Override
	public ActionResult verify(AnalyzedInterface input) {
		if (input
			.getInterfaces()
			.stream()
			.map(GenerifiedName::getName)
			.map(this::stripWhitespaces)
			.anyMatch(stripWhitespaces(interfaceName)::equals)) {
			return ActionResult.ok();
		}

		return ActionResult
			.error("Interface does not extend %s (observed: %s)", interfaceName, input.getInterfaces().stream().map(GenerifiedName::getName).collect(Collectors
																																						 .joining(", ")));
	}

	private String stripWhitespaces(String s) {
		return s.replaceAll("\\s", "");
	}

}
