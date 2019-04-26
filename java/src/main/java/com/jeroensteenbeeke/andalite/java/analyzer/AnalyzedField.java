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

package com.jeroensteenbeeke.andalite.java.analyzer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.javaparser.ast.Modifier;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

import java.util.List;

public final class AnalyzedField extends AccessModifiable<AnalyzedField, AnalyzedField.FieldInsertionPoint> {
	private final String name;

	private final AnalyzedType type;

	private Location specificDeclarationLocation;

	private AnalyzedExpression initializationExpression;

	public AnalyzedField(@Nonnull Location location, List<Modifier.Keyword> modifiers,
			@Nonnull String name, @Nonnull AnalyzedType type) {
		super(location, modifiers);
		this.name = name;
		this.type = type;
		this.specificDeclarationLocation = location;
		this.initializationExpression = null;
	}

	public Location getSpecificDeclarationLocation() {
		return specificDeclarationLocation;
	}

	void setSpecificDeclarationLocation(Location specificDeclarationLocation) {
		this.specificDeclarationLocation = specificDeclarationLocation;
	}

	void setInitializationExpression(AnalyzedExpression initializationExpression) {
		this.initializationExpression = initializationExpression;
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public AnalyzedType getType() {
		return type;
	}

	@CheckForNull
	public AnalyzedExpression getInitializationExpression() {
		return initializationExpression;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write(type.toJavaString());
		callback.write(" ");
		callback.write(name);
		callback.write(";");
	}

	@Override
	public FieldInsertionPoint getAnnotationInsertPoint() {
		return FieldInsertionPoint.BEFORE;
	}

	public enum FieldInsertionPoint implements IInsertionPoint<AnalyzedField> {
		BEFORE {
			@Override
			public int position(AnalyzedField container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(AnalyzedField container) {
				return container.getLocation().getEnd();
			}
		}, BEFORE_SEMICOLON {
			@Override
			public int position(AnalyzedField container) {
				return container.getLocation().getEnd() - 1;
			}

		};
	}
}
