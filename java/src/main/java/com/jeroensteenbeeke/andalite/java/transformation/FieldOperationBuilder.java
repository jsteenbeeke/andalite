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
package com.jeroensteenbeeke.andalite.java.transformation;

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.FieldNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IFieldOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureFieldAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureFieldInitialization;

public class FieldOperationBuilder
		extends AbstractOperationBuilder<AnalyzedField, IFieldOperation> {
	FieldOperationBuilder(IStepCollector collector,
			IJavaNavigation<? extends ContainingDenomination<?,?>> parentNav, String fieldName) {
		super(collector, new FieldNavigation<>(parentNav, fieldName));
	}

	@NotNull
	public AnnotatableOperationBuilder<AnalyzedField> forAnnotation(
			@NotNull String type) {
		return new AnnotatableOperationBuilder<>(getCollector(),
												 getNavigation(), type);
	}

	public void ensureAnnotation(@NotNull String annotation) {
		ensure(new EnsureFieldAnnotation(annotation));
	}

	public void ensureInitialization(@NotNull String expression) {
		ensure(new EnsureFieldInitialization(expression));
	}

}
