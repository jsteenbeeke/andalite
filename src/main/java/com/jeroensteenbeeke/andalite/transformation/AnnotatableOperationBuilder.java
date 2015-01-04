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
package com.jeroensteenbeeke.andalite.transformation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.Annotatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.AnnotationNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.INavigation;
import com.jeroensteenbeeke.andalite.transformation.operations.IAnnotationOperation;

public class AnnotatableOperationBuilder<T extends Annotatable> extends
		AbstractOperationBuilder<AnalyzedAnnotation, IAnnotationOperation>
		implements
		IAnnotationOperationBuilder<AnalyzedAnnotation, IAnnotationOperation> {
	AnnotatableOperationBuilder(IStepCollector collector,
			INavigation<T> parentNav, String annotationName) {
		super(collector, new AnnotationNavigation<T>(parentNav, annotationName));
	}

	public AnnotationFieldOperationBuilderBuilder forAnnotationField(String name) {

		return new AnnotationFieldOperationBuilderBuilder(getCollector(),
				getNavigation(), name);
	}
}
