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
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.lux.ActionResult;
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

	private boolean staticField = false;

	private boolean finalField = false;

	private boolean volatileField = false;

	public EnsureField(@Nonnull String name, @Nonnull String type,
			@Nonnull AccessModifier modifier) {
		super();
		this.name = name;
		this.type = type;
		this.modifier = modifier;
	}

	public EnsureField shouldBeStatic() {
		this.staticField = true;
		return this;
	}

	public EnsureField shouldBeFinal() {
		this.finalField = true;
		return this;
	}

	public EnsureField shouldBeVolatile() {
		this.volatileField = true;
		return this;
	}

	@Override
	public List<Transformation> perform(@Nonnull AnalyzedClass input)
			throws OperationException {
		if (input.hasField(name)) {
			AnalyzedField field = input.getField(name);

			ImmutableList.Builder<Transformation> transforms = ImmutableList
					.builder();

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

				if (field.isFinal() != finalField) {
					if (finalField) {
						transforms.add(Transformation.insertBefore(
								field.getType(), " final "));
					} else {
						throw new OperationException(String.format(
								"Field %s should not be final, but is",
								field.getName()));
					}
				}
				if (field.isStatic() != staticField) {
					if (staticField) {
						transforms.add(Transformation.insertBefore(
								field.getType(), " static "));
					} else {
						throw new OperationException(String.format(
								"Field %s should not be static, but is",
								field.getName()));
					}
				}
				if (field.isVolatile() != volatileField) {
					if (volatileField) {
						transforms.add(Transformation.insertBefore(
								field.getType(), " volatile "));
					} else {
						throw new OperationException(String.format(
								"Field %s should not be volatile, but is",
								field.getName()));
					}
				}
			}

			return transforms.build();
		}

		List<String> extras = Lists.newArrayList();
		if (staticField) {
			extras.add("static ");
		}
		if (finalField) {
			extras.add("final ");
		}
		if (volatileField) {
			extras.add("volatile ");
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
				"\n\t%s%s%s %s;\n\n", modifier.getOutput(), extras.stream()
						.collect(Collectors.joining()), type, name)));

		return transforms.build();
	}

	@Override
	public String getDescription() {
		return String.format("presence of field: %s %s %s",
				modifier.getOutput(), type, name);
	}

	@Override
	public ActionResult verify(AnalyzedClass input) {
		if (input.hasField(name)) {
			AnalyzedField field = input.getField(name);

			if (!field.getType().toJavaString().equals(type)) {
				return ActionResult.error(
						"Invalid field type: %s (expected %s)", field.getType()
								.toJavaString(), type);
			}

			if (field.getAccessModifier() != modifier) {
				return ActionResult.error(
						"Invalid field access modifier: %s (expected %s)",
						field.getAccessModifier(), modifier);
			}

			if (field.isFinal() != finalField) {
				return ActionResult.error(
						"Invalid final modifier: %s (expected %s)",
						field.isFinal(), finalField);
			}

			if (field.isStatic() != staticField) {
				return ActionResult.error(
						"Invalid static modifier: %s (expected %s)",
						field.isStatic(), staticField);
			}

			return ActionResult.ok();
		}

		return ActionResult.error("Class does not have field %s", name);
	}

}
