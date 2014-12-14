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
package com.jeroensteenbeeke.andalite.transformation.navigation;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.Annotatable;

public class AnnotationNavigation<T extends Annotatable> extends
		ChainedNavigation<T, AnalyzedAnnotation> {
	private final String annotationType;

	public AnnotationNavigation(Navigation<T> chained, String annotationType) {
		super(chained);
		this.annotationType = annotationType;
	}

	@Override
	public AnalyzedAnnotation navigate(T chainedTarget)
			throws NavigationException {
		if (chainedTarget.hasAnnotation(annotationType)) {
			AnalyzedAnnotation annotation = chainedTarget
					.getAnnotation(annotationType);
			if (annotation != null) {
				return annotation;
			}
		}

		throw new NavigationException("Target has no annotation of type %s",
				annotationType);
	}

	@Override
	public String getDescription() {
		return String.format("Annotation of type %s", annotationType);
	}
}
