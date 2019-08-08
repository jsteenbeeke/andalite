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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedParameter;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IParameterOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureParameterAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.RemoveParameterAnnotation;

public class ParameterScopeOperationBuilder extends
		AbstractOperationBuilder<AnalyzedParameter, IParameterOperation> {

	ParameterScopeOperationBuilder(IStepCollector collector,
			IJavaNavigation<AnalyzedParameter> navigation) {
		super(collector, navigation);
	}

	public void ensureAnnotation(@Nonnull String annotation) {
		ensure(new EnsureParameterAnnotation(annotation));
	}

	public void removeAnnotation(@Nonnull String annotation) {
		ensure(new RemoveParameterAnnotation(annotation));
	}

	public AnnotatableOperationBuilder<AnalyzedParameter> forAnnotation(String name) {
		return new AnnotatableOperationBuilder<>(getCollector(), getNavigation(), name);
	}
}
