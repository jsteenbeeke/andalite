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

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassDefinition;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedImport;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.operations.ICompilationUnitOperation;

public class EnsureImports implements ICompilationUnitOperation {
	private static final String WILDCARD = "*";
	private final String fqdn;

	public EnsureImports(@NotNull String fqdn) {
		// Assume developers follow Java conventions and don't use
		// lowercase class names
		if (fqdn.toLowerCase().equals(fqdn) && !fqdn.endsWith(WILDCARD)) {
			throw new IllegalArgumentException(
					"Invalid fully qualified domain name: " + fqdn);
		}
		
		if (!fqdn.contains(".")) {
			throw new IllegalArgumentException(
					"Invalid fully qualified domain name: " + fqdn);
		}

		this.fqdn = fqdn;
	}

	@Override
	public List<Transformation> perform(@NotNull AnalyzedSourceFile input) {
		ClassDefinition def = ClassDefinition.fromFQDN(input.getFullyQualifiedName());
		ClassDefinition desired = ClassDefinition.fromFQDN(fqdn);

		if (desired.getPackageName().equals(def.getPackageName())) {
			// No need to import classes from same package
			return ImmutableList.of();
		}

		for (AnalyzedImport analyzedImport : input.getImports()) {
			if (analyzedImport.matchesClass(fqdn)) {
				return ImmutableList.of();
			}
		}

		return ImmutableList.of(input.insertAt(AnalyzedSourceFile.SourceFileInsertionPoint.AFTER_IMPORTS, String.format("\nimport %s;", fqdn)));
	}

	@Override
	public String getDescription() {
		return String.format("import of class %s", fqdn);
	}

	@Override
	public ActionResult verify(@NotNull AnalyzedSourceFile input) {
		ClassDefinition def = ClassDefinition.fromFQDN(input.getFullyQualifiedName());
		ClassDefinition desired = ClassDefinition.fromFQDN(fqdn);

		if (desired.getPackageName().equals(def.getPackageName())) {
			// No need to import classes from same package
			return ActionResult.ok();
		}

		if (input.hasImport(fqdn)) {
			return ActionResult.ok();
		}

		return ActionResult.error("Class does not import %s", fqdn);
	}
}
