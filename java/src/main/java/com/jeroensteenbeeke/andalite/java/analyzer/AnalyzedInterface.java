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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AnalyzedInterface extends ContainingDenomination<AnalyzedInterface, AnalyzedInterface.InterfaceInsertionPoint> {

	public AnalyzedInterface(@Nonnull AnalyzedSourceFile sourceFile,
							 @Nonnull Location location,
							 @Nonnull List<Modifier.Keyword> modifiers,
							 @Nonnull String packageName,
							 @Nonnull LocatedName<SimpleName> name) {
		super(sourceFile, location, modifiers, packageName, name);
	}

	public String getInterfaceName() {
		return getDenominationName();
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("interface ");
		callback.write(getInterfaceName());
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		for (AnalyzedField analyzedField : getFields()) {
			analyzedField.output(callback);
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

	@Nonnull
	@Override
	public Transformation insertAt(@Nonnull InterfaceInsertionPoint insertionPoint, @Nonnull String replacement) {
		if (insertionPoint == InterfaceInsertionPoint.AT_SUPERINTERFACE_POINT && getInterfaces().isEmpty()) {
			return super.insertAt(insertionPoint, " extends " + replacement);
		}

		return super.insertAt(insertionPoint, replacement);
	}

	@Override
	public InterfaceInsertionPoint getAnnotationInsertPoint() {
		return InterfaceInsertionPoint.BEFORE;
	}

	public enum InterfaceInsertionPoint implements IInsertionPoint<AnalyzedInterface> {
		BEFORE {
			@Override
			public int position(AnalyzedInterface container) {
				return container.getLocation().getStart();
			}
		},
		AT_SUPERINTERFACE_POINT {
			@Override
			public int position(AnalyzedInterface container) {
				return container.getLastImplementsLocation().or(() -> Optional.of(container.getNameLocation()))
								.map(Location::getEnd)
								.map(e -> e + 1)
								.orElseThrow(() -> new IllegalStateException("Could not determine superinterface target location"));
			}
		}, START {
			@Override
			public int position(AnalyzedInterface container) {
				return container.getBodyLocation()
								.map(Location::getStart)
								.orElseThrow(() -> new IllegalStateException("Interface without body location"));
			}
		}, END {
			@Override
			public int position(AnalyzedInterface container) {
				return container.getBodyLocation()
								.map(Location::getEnd)
								.orElseThrow(() -> new IllegalStateException("Interface without body location"));
			}
		}, BEFORE_FIRST_METHOD {
			@Override
			public int position(AnalyzedInterface container) {
				return container.getMethods().stream().map(AnalyzedMethod::getLocation)
								.reduce(Location::min)
								.map(Location::getStart)
								.map(start -> start - 1)
								.orElseGet(() -> START.position(container));
			}
		}, AFTER_LAST_METHOD {
			@Override
			public int position(AnalyzedInterface container) {
				return container.getMethods().stream().map(AnalyzedMethod::getLocation)
								.reduce(Location::max)
								.map(Location::getEnd)
								.map(end -> end + 1)
								.orElseGet(() -> END.position(container));
			}
		};
	}
}
