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
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public final class AnalyzedClass extends ConstructableDenomination<AnalyzedClass, AnalyzedClass.ClassInsertionPoint> {

	private GenerifiedName superClass = null;

	public AnalyzedClass(@Nonnull AnalyzedSourceFile sourceFile, @Nonnull Location location, @Nonnull List<Modifier.Keyword> modifiers,
						 @Nonnull String packageName, @Nonnull LocatedName<SimpleName> name) {
		super(sourceFile, location, modifiers, packageName, name);
	}

	public String getClassName() {
		return getDenominationName();
	}

	@Nonnull
	public Optional<GenerifiedName> getSuperClass() {
		return Optional.ofNullable(superClass);
	}

	void setSuperClass(GenerifiedName superClass) {
		this.superClass = superClass;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("class ");
		callback.write(getClassName());
		if (superClass != null) {
			callback.write(" extends ");
			callback.write(superClass.getName());
		}

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

	@Override
	public ClassInsertionPoint getAnnotationInsertPoint() {
		return ClassInsertionPoint.BEFORE;
	}


	public enum ClassInsertionPoint implements IInsertionPoint<AnalyzedClass> {
		BEFORE {
			@Override
			public int position(AnalyzedClass container) {
				return container.getLocation().getStart();
			}
		},
		START {
			@Override
			public int position(AnalyzedClass container) {
				return container.getBodyLocation()
								.map(Location::getStart)
								.orElseThrow(() -> new IllegalStateException("Class without body location"));
			}
		}, END {
			@Override
			public int position(AnalyzedClass container) {
				return container.getBodyLocation()
								.map(Location::getEnd)
								.orElseThrow(() -> new IllegalStateException("Class without body location"));
			}
		}, BEFORE_FIRST_METHOD {
			@Override
			public int position(AnalyzedClass container) {
				return container.getMethods().stream().map(AnalyzedMethod::getLocation)
								.reduce(Location::min)
								.map(Location::getStart)
								.map(start -> start - 1)
								.orElseGet(() -> START.position(container));
			}
		}, AFTER_LAST_METHOD {
			@Override
			public int position(AnalyzedClass container) {
				return container.getMethods().stream().map(AnalyzedMethod::getLocation)
								.reduce(Location::max)
								.map(Location::getEnd)
								.map(end -> end + 1)
								.orElseGet(() -> END.position(container));
			}
		}, BEFORE_FIRST_FIELD {
			@Override
			public int position(AnalyzedClass container) {
				return container.getFields().stream().map(AnalyzedField::getLocation)
								.reduce(Location::min)
								.map(Location::getStart)
								.map(start -> start - 1)
								.orElseGet(() -> START.position(container));
			}
		}, AFTER_LAST_FIELD {
			@Override
			public int position(AnalyzedClass container) {
				return container.getFields().stream().map(AnalyzedField::getLocation)
								.reduce(Location::max)
								.map(Location::getEnd)
								.map(end -> end + 1)
								.orElseGet(() -> END.position(container));
			}
		}, AFTER_LAST_IMPLEMENTED_INTERFACE {
			@Override
			public int position(AnalyzedClass container) {
				return container.getLastImplementsLocation()
								.map(Location::getEnd)
								.map(end -> end + 1)
								.orElseGet(() -> AFTER_SUPERCLASS.position(container));
			}
		}, AFTER_SUPERCLASS {
			@Override
			public int position(AnalyzedClass container) {
				return container.getSuperClass().map(GenerifiedName::getLocation)
								.map(Location::getEnd)
								.map(end -> end + 1)
								.orElseGet(() -> AFTER_NAME.position(container));
			}
		}, AFTER_NAME {
			@Override
			public int position(AnalyzedClass container) {
				return container.getNameLocation().getEnd() + 1;
			}
		};
	}
}
