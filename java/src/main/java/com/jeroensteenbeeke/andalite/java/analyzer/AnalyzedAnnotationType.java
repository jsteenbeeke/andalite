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

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public class AnalyzedAnnotationType extends ContainingDenomination<AnalyzedAnnotationType, AnalyzedAnnotationType.AnnotationTypeInsertionPoint> {

	protected AnalyzedAnnotationType(@NotNull AnalyzedSourceFile sourceFile, @NotNull Location location, @NotNull List<Modifier.Keyword> modifiers,
									 @NotNull String packageName, @NotNull LocatedName<SimpleName> name) {
		super(sourceFile, location, modifiers, packageName, name);
	}

	public String getAnnotationName() {
		return getDenominationName();
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("@interface ");
		callback.write(getAnnotationName());
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();

		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
		callback.newline();
	}

	@Override
	public AnnotationTypeInsertionPoint getAnnotationInsertPoint() {
		return AnnotationTypeInsertionPoint.BEFORE;
	}

	public enum AnnotationTypeInsertionPoint implements IInsertionPoint<AnalyzedAnnotationType> {
		BEFORE {
			@Override
			public int position(AnalyzedAnnotationType container) {
				return container.getLocation().getStart();
			}
		},
		START {
			@Override
			public int position(AnalyzedAnnotationType container) {
				return container.getBodyLocation()
					.map(Location::getStart)
					.orElseThrow(() -> new IllegalStateException("Annotation definition without body location"));
			}
		}, END {
			@Override
			public int position(AnalyzedAnnotationType container) {
				return container.getBodyLocation()
					.map(Location::getEnd)
					.orElseThrow(() -> new IllegalStateException("Annotation definition without body location"));
			}
		}
	}
}
