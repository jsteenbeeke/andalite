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
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.operations.ICompilationUnitOperation;

/**
 * Ensures that a given compilation unit will have an interface with default
 * (package) scope and the specified name
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class EnsurePackageInterface implements ICompilationUnitOperation {
	private final String expectedInterfaceName;

	/**
	 * Create a new EnsurePackageInterface operation
	 * 
	 * @param expectedInterfaceName
	 *            The name of the interface we want to ensure the existence of.
	 *            Needs to be a valid Java identifier
	 */
	public EnsurePackageInterface(String expectedInterfaceName) {
		if (!isValidJavaIdentifier(expectedInterfaceName)) {
			throw new IllegalArgumentException(
					String.format("%s is not a valid Java identifier",
							expectedInterfaceName));
		}
		this.expectedInterfaceName = expectedInterfaceName;
	}

	@Override
	public List<Transformation> perform(AnalyzedSourceFile input) {
		ILocatable last = input;

		for (AnalyzedInterface iface : input.getInterfaces()) {
			if (iface.getAccessModifier() == AccessModifier.DEFAULT
					&& expectedInterfaceName.equals(iface.getInterfaceName())) {
				return ImmutableList.of();
			}

			last = iface;
		}

		return ImmutableList.of(Transformation.insertAt(
				last.getLocation().getEnd() + 2,
				String.format("interface %s {\n\n}\n", expectedInterfaceName)));
	}

	@Override
	public String getDescription() {
		return String.format("presence of a package-level interface named %s",
				expectedInterfaceName);
	}

	@Override
	public ActionResult verify(AnalyzedSourceFile input) {
		for (AnalyzedInterface iface : input.getInterfaces()) {
			if (iface.getAccessModifier() == AccessModifier.DEFAULT
					&& expectedInterfaceName.equals(iface.getInterfaceName())) {
				return ActionResult.ok();
			}
		}
		return ActionResult.error("No package interface named %s",
				expectedInterfaceName);
	}
}
