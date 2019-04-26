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

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedEnum;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.operations.ICompilationUnitOperation;

public class EnsurePublicEnum implements ICompilationUnitOperation {

	@Override
	public List<Transformation> perform(AnalyzedSourceFile input) {
		for (AnalyzedEnum en : input.getEnums()) {
			if (en.getAccessModifier() == AccessModifier.PUBLIC) {
				return ImmutableList.of();
			}
		}

		return ImmutableList.of(input.insertAt(AnalyzedSourceFile.SourceFileInsertionPoint.AFTER_LAST_DENOMINATION,
											   String.format("public enum %s {\n\n}\n",
															 stripExtension(input.getOriginalFile().getName()))));
	}

	private String stripExtension(@Nonnull String name) {
		StringBuilder builder = new StringBuilder();

		for (char c : name.toCharArray()) {
			if (c == '.') {
				break;
			}

			builder.append(c);
		}

		return builder.toString();
	}

	@Override
	public String getDescription() {
		return "presence of a public enum with name similar to compilation unit";
	}

	@Override
	public ActionResult verify(AnalyzedSourceFile input) {
		for (AnalyzedEnum en : input.getEnums()) {
			if (en.getAccessModifier() == AccessModifier.PUBLIC) {
				return ActionResult.ok();
			}
		}

		return ActionResult.error("No public enums in compilation unit");
	}
}
