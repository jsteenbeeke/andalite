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
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.lux.ActionResult;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public abstract class EnsureField<T extends ContainingDenomination<T,I>,I extends Enum<I> & IInsertionPoint<T>,E extends EnsureField<T,I,E>> implements IJavaOperation<T> {
	private final String name;

	private final String type;

	private final AccessModifier modifier;

	private boolean staticField = false;

	private boolean finalField = false;

	private boolean volatileField = false;

	public EnsureField(@NotNull String name, @NotNull String type,
					   @NotNull AccessModifier modifier) {
		super();
		this.name = name;
		this.type = type;
		this.modifier = modifier;
	}

	@SuppressWarnings("unchecked")
	public E shouldBeStatic() {
		this.staticField = true;
		return (E) this;
	}

	@SuppressWarnings("unchecked")
	public E shouldBeFinal() {
		this.finalField = true;
		return (E) this;
	}

	@SuppressWarnings("unchecked")
	public E shouldBeVolatile() {
		this.volatileField = true;
		return (E) this;
	}

	@Override
	public List<Transformation> perform(@NotNull T input)
		throws OperationException {
		AnalyzedField field = getField(input, name);

		if (field != null) {

			ImmutableList.Builder<Transformation> transforms = ImmutableList
				.builder();

			// Findbugs, implied by input.hasField
			AnalyzedType analyzedType = field.getType();
			if (!type.equals(analyzedType.toJavaString())) {
				throw new OperationException(
					String.format(
						"Field %s should have type %s but instead has type %s",
						name, type, analyzedType.toJavaString()));
			}

			if (!modifier.equals(field.getAccessModifier())) {
				throw new OperationException(
					String.format(
						"Field %s should have access modifier %s but instead has access modifier %s",
						name, modifier, field.getAccessModifier()));
			}

			if (field.isFinal() != finalField) {
				if (finalField) {
					transforms.add(field.insertAt(AnalyzedField.FieldInsertionPoint.BEFORE, " final "));
				} else {
					throw new OperationException(String.format(
						"Field %s should not be final, but is",
						field.getName()));
				}
			}
			if (field.isStatic() != staticField) {
				if (staticField) {
					transforms.add(field.insertAt(AnalyzedField.FieldInsertionPoint.BEFORE, " static "));
				} else {
					throw new OperationException(String.format(
						"Field %s should not be static, but is",
						field.getName()));
				}
			}
			if (field.isVolatile() != volatileField) {
				if (volatileField) {
					transforms.add(field.insertAt(AnalyzedField.FieldInsertionPoint.BEFORE, " volatile "));
				} else {
					throw new OperationException(String.format(
						"Field %s should not be volatile, but is",
						field.getName()));
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

		ImmutableList.Builder<Transformation> transforms = ImmutableList
			.builder();

		if (getBodyLocation(input).map(Location::getLength).orElse(0) == 0) {
			extras.add(0, "\n\n");
		}


		transforms.add(input.insertAt(getLastFieldLocation(), String.format(
			"\n\t%s%s%s %s;\n\n", modifier.getOutput(), String.join("", extras), type, name)));

		return transforms.build();
	}

	protected abstract I getLastFieldLocation();

	public Optional<Location> getBodyLocation(T input) {
		return Optional.empty();
	}

	protected abstract AnalyzedField getField(T input, String name);

	@Override
	public String getDescription() {
		return String.format("presence of field: %s %s %s",
							 modifier.getOutput(), type, name);
	}

	@Override
	public ActionResult verify(@NotNull T input) {
		AnalyzedField field = getField(input, name);
		if (field != null) {

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
