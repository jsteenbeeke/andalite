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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.annotation.AnnotationValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.ArrayValue;
import com.jeroensteenbeeke.andalite.analyzer.annotation.BaseValue;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.annot.InnerAnnotationCondition;

public class AnnotationFieldNavigation extends
		ChainedNavigation<AnalyzedAnnotation, AnalyzedAnnotation> {

	private final String fieldName;

	private final InnerAnnotationCondition condition;

	public AnnotationFieldNavigation(
			@Nonnull Navigation<AnalyzedAnnotation> chained,
			@Nullable String fieldName,
			@Nullable InnerAnnotationCondition condition) {
		super(chained);
		this.fieldName = fieldName;
		this.condition = condition;
	}

	@Override
	public AnalyzedAnnotation navigate(AnalyzedAnnotation target)
			throws NavigationException {
		if (target.hasValueOfType(AnnotationValue.class, fieldName)) {
			AnnotationValue value = target.getValue(AnnotationValue.class,
					fieldName);

			if (condition == null || condition.isSatisfiedBy(value)) {
				return value.getValue();
			}
		} else if (condition != null
				&& target.hasValueOfType(ArrayValue.class, fieldName)) {
			ArrayValue array = target.getValue(ArrayValue.class, fieldName);

			for (BaseValue<?> baseValue : array.getValue()) {
				if (baseValue instanceof AnnotationValue) {
					AnnotationValue annot = (AnnotationValue) baseValue;

					if (condition.isSatisfiedBy(annot)) {
						return annot.getValue();
					}
				}
			}
		}

		throw new NavigationException(
				"Could not find annotation field named %s that satisfies specified conditions: %s",
				fieldName != null ? fieldName : null, condition.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Annotation field named %s",
				fieldName != null ? fieldName : "value");
	}

}
