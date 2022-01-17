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
package com.jeroensteenbeeke.andalite.java.analyzer;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.expr.SimpleName;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;

import javax.annotation.CheckForNull;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public class AnalyzedEnum extends
	ConstructableDenomination<AnalyzedEnum, AnalyzedEnum.EnumInsertionPoint> {
	private final List<AnalyzedEnumConstant> constants;

	private Location separatorLocation;

	public AnalyzedEnum(@NotNull AnalyzedSourceFile sourceFile,
		@NotNull Location location, @NotNull List<Modifier.Keyword> modifiers,
		@NotNull String packageName, @NotNull LocatedName<SimpleName> name) {
		super(sourceFile, location, modifiers, packageName, name);
		this.constants = Lists.newArrayList();
	}

	@NotNull
	public String getEnumName() {
		return getDenominationName();
	}

	public void setSeparatorLocation(@NotNull Location separatorLocation) {
		this.separatorLocation = separatorLocation;
	}

	@CheckForNull
	public Location getSeparatorLocation() {
		return separatorLocation;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("enum ");
		callback.write(getEnumName());
		outputInterfaces(callback);
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		for (AnalyzedField analyzedField : getFields()) {
			analyzedField.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedConstructor constructor : getConstructors()) {
			constructor.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedMethod analyzedMethod : getMethods()) {
			analyzedMethod.output(callback);
			callback.newline();
			callback.newline();
		}

		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
		callback.newline();

	}

	void addConstant(@NotNull AnalyzedEnumConstant constant) {
		constants.add(constant);
	}

	@NotNull
	public List<AnalyzedEnumConstant> getConstants() {
		return ImmutableList.copyOf(constants);
	}

	@NotNull
	@Override
	public Transformation insertAt(@NotNull EnumInsertionPoint insertionPoint,
		@NotNull String replacement) {
		if (getSeparatorLocation() == null && insertionPoint
			.isAfterSeparator()) {
			return super.insertAt(EnumInsertionPoint.AFTER_LAST_CONSTANT,
				"; " + replacement);
		}

		return super.insertAt(insertionPoint, replacement);
	}

	@Override
	public EnumInsertionPoint getAnnotationInsertPoint() {
		return EnumInsertionPoint.BEFORE;
	}

	public enum EnumInsertionPoint implements IInsertionPoint<AnalyzedEnum> {
		BEFORE {
			@Override
			public int position(AnalyzedEnum container) {
				return container.getLocation().getStart();
			}
		}, AFTER_ENUM_NAME {
			@Override
			public int position(AnalyzedEnum container) {
				return container.getNameLocation().getEnd() + 1;
			}
		}, AFTER_LAST_INTERFACE {
			@Override
			public int position(AnalyzedEnum container) {
				return container.getLastImplementsLocation()
					.map(Location::getEnd).map(e -> e + 1)
					.orElseGet(() -> AFTER_ENUM_NAME.position(container));
			}
		}, BEFORE_FIRST_CONSTANT {
			@Override
			public int position(AnalyzedEnum container) {
				return container.getBodyLocation().map(Location::getStart)
					.orElseThrow(() -> new IllegalStateException(
						"Enum without body location"));
			}
		}, AFTER_LAST_CONSTANT {
			@Override
			public int position(AnalyzedEnum container) {
				return Optional.ofNullable(container.getSeparatorLocation())
					.map(Location::getStart)
					.orElseGet(() -> END_OF_IMPLEMENTATION.position(container));
			}
		}, START_OF_IMPLEMENTATION {
			@Override
			public int position(AnalyzedEnum container) {
				return Optional.ofNullable(container.getSeparatorLocation())
					.map(Location::getEnd).map(e -> e + 1)
					.orElseGet(() -> END_OF_IMPLEMENTATION.position(container));
			}

			@Override
			public boolean isAfterSeparator() {
				return true;
			}
		}, END_OF_IMPLEMENTATION {
			@Override
			public int position(AnalyzedEnum container) {
				return container.getBodyLocation().map(Location::getEnd)
					.map(e -> e - 1).orElseThrow(
						() -> new IllegalStateException(
							"Enum without body location"));
			}

			@Override
			public boolean isAfterSeparator() {
				return true;
			}
		};

		public boolean isAfterSeparator() {
			return false;
		}
	}
}
