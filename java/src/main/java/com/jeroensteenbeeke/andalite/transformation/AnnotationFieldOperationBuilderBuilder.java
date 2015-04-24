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
import com.jeroensteenbeeke.andalite.transformation.navigation.AnnotationFieldNavigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.INavigation;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.annot.InnerAnnotationCondition;

public class AnnotationFieldOperationBuilderBuilder {

	final IStepCollector collector;

	final INavigation<AnalyzedAnnotation> navigation;

	final String name;

	public AnnotationFieldOperationBuilderBuilder(IStepCollector collector,
			INavigation<AnalyzedAnnotation> navigation, String name) {
		this.collector = collector;
		this.navigation = navigation;
		this.name = name;
	}

	public NavigateToInnerAnnotationConditionBuilder inArray() {
		return new NavigateToInnerAnnotationConditionBuilder(this);
	}

	public AnnotationFieldOperationBuilder ifNotAnArray() {
		return new AnnotationFieldOperationBuilder(collector,
				new AnnotationFieldNavigation(navigation, name, null));
	}

	public AnnotationFieldOperationBuilder getBuilderForCondition(
			InnerAnnotationCondition finalCondition) {

		return new AnnotationFieldOperationBuilder(collector,
				new AnnotationFieldNavigation(navigation, name, finalCondition));
	}

}
