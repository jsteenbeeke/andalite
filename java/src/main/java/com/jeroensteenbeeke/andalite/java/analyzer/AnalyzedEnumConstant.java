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

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AnalyzedEnumConstant extends ContainingDenomination<AnalyzedEnumConstant, AnalyzedEnumConstant.EnumConstantInsertionPoint> {
	private final List<AnalyzedExpression> parameters;

	public AnalyzedEnumConstant(@NotNull AnalyzedSourceFile sourceFile, @NotNull Location location, @NotNull List<Modifier.Keyword> modifiers,
								@NotNull String packageName, @NotNull LocatedName<SimpleName> name, @NotNull List<AnalyzedExpression> parameters) {
		super(sourceFile, location, modifiers, packageName, name);
		this.parameters = parameters;
	}

	public List<AnalyzedExpression> getParameters() {
		return parameters;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		outputInterfaces(callback);
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

	@NotNull
	@Override
	public Transformation insertAt(@NotNull EnumConstantInsertionPoint insertionPoint, @NotNull String code) {
		String actualCode = code;

		if (insertionPoint.requiresBody() && getBodyLocation().isEmpty()) {
			actualCode = " { "+ actualCode + " } ";
		}

		return super.insertAt(insertionPoint, actualCode);
	}

	@Override
	public EnumConstantInsertionPoint getAnnotationInsertPoint() {
		return EnumConstantInsertionPoint.BEFORE;
	}

	public enum EnumConstantInsertionPoint implements IInsertionPoint<AnalyzedEnumConstant> {
		BEFORE {
			@Override
			public int position(AnalyzedEnumConstant container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(AnalyzedEnumConstant container) {
				return container.getLocation().getEnd() + 1;
			}
		}, BEFORE_FIRST_MEMBER(true) {
			@Override
			public int position(AnalyzedEnumConstant container) {
				return container.getBodyLocation().map(Location::getStart)
								.map(s -> s + 1)
								.orElseGet(() -> AFTER.position(container));
			}
		},
		AFTER_LAST_MEMBER(true) {
			@Override
			public int position(AnalyzedEnumConstant container) {
				return container.getBodyLocation().map(Location::getEnd)
								.map(e -> e - 1)
								.orElseGet(() -> AFTER.position(container));
			}
		};

		private final boolean requiresBody;

		EnumConstantInsertionPoint(boolean requiresBody) {
			this.requiresBody = requiresBody;
		}

		EnumConstantInsertionPoint() {
			this(false);
		}

		public boolean requiresBody() {
			return requiresBody;
		}
	}
}
