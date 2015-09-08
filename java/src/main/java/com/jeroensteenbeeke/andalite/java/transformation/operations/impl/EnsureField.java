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

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;

public class EnsureField implements IClassOperation {
	private final String name;

	private final String type;

	private final AccessModifier modifier;

	public EnsureField(@Nonnull String name, @Nonnull String type,
			@Nonnull AccessModifier modifier) {
		super();
		this.name = name;
		this.type = type;
		this.modifier = modifier;
	}

	@Override
	public List<Transformation> perform(@Nonnull AnalyzedClass input)
			throws OperationException {
		if (input.hasField(name)) {
			AnalyzedField field = input.getField(name);

			if (field != null) { // Findbugs, implied by input.hasField
				AnalyzedType analyzedType = field.getType();
				if (analyzedType != null
						&& !type.equals(analyzedType.toJavaString())) {
					throw new OperationException(
							String.format(
									"Field %s should have type %s but instead has type %s",
									name, type, analyzedType));
				}

				if (!modifier.equals(field.getAccessModifier())) {
					throw new OperationException(
							String.format(
									"Field %s should have access modifier %s but instead has access modifier %s",
									name, modifier, field.getAccessModifier()));
				}
			}

			return ImmutableList.of();
		}

		Location location = input.getBodyLocation();

		for (AnalyzedField field : input.getFields()) {
			location = field.getLocation();
		}

		if (location == null) {
			throw new OperationException(
					"Cannot determine insertion point for new field");
		}

		ImmutableList.Builder<Transformation> transforms = ImmutableList
				.builder();

		int l = location.getEnd() + 1;

		if (location.getLength() == 0) {
			transforms.add(Transformation.insertAt(location.getStart() + 2,
					"\n\n"));

			l = location.getStart() + 3;
		}

		transforms.add(Transformation.insertAt(l, String.format(
				"\n\t%s%s %s;\n\n", modifier.getOutput(), type, name)));

		return transforms.build();
	}

	@Override
	public String getDescription() {
		return String.format("presence of field: %s %s %s",
				modifier.getOutput(), type, name);
	}

}
