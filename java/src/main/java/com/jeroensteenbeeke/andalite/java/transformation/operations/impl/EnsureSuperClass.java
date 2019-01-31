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

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;

import javax.annotation.Nonnull;

public class EnsureSuperClass implements IClassOperation {
	private final String superClass;

	public EnsureSuperClass(String superClass) {
		this.superClass = superClass;
	}

	@Override
	public List<Transformation> perform(AnalyzedClass input)
			throws OperationException {
		if (superClass.equals(input.getSuperClass())) {
			return ImmutableList.of();
		} else {
			Location extendsLocation = input.getExtendsLocation();
			if (input.getSuperClass() != null && extendsLocation != null) {
				return ImmutableList.of(Transformation.replace(extendsLocation,
						superClass));
			} else {
				if (extendsLocation != null) {
					if (extendsLocation.getLength() == 0) {
						// Prefix with space
						return ImmutableList
								.of(Transformation.insertAfter(extendsLocation,
										" extends ".concat(superClass)));
					} else {
						return ImmutableList
								.of(Transformation.insertAfter(extendsLocation,
										"extends ".concat(superClass)));
					}
				} else {
					// Rare
					return ImmutableList.of(Transformation.insertAfter(
							input.getNameLocation(),
							" extends ".concat(superClass)));
				}
			}
		}
	}

	@Override
	public String getDescription() {
		return "ensure superclass is ".concat(superClass);
	}

	@Override
	public ActionResult verify(@Nonnull AnalyzedClass input) {
		if (stripWhitespaces(superClass).equals(stripWhitespaces(input.getSuperClass()))) {
			return ActionResult.ok();
		}
		return ActionResult.error("Invalid superclass: %s, expected %s",
				input.getSuperClass(), superClass);
	}


	private String stripWhitespaces(String s) {
		if (s == null) {
			return null;
		}
		
		return s.replaceAll("\\s", "");
	}
}
