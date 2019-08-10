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

package com.jeroensteenbeeke.andalite.java.transformation.navigation;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedField;
import com.jeroensteenbeeke.andalite.java.analyzer.ContainingDenomination;

public class FieldNavigation<T extends ContainingDenomination<?,?>> extends
		ChainedNavigation<T, AnalyzedField> {
	private final String fieldName;

	public FieldNavigation(@Nonnull IJavaNavigation<T> chained,
			@Nonnull String fieldName) {
		super(chained);
		this.fieldName = fieldName;
	}

	@Override
	@Nonnull
	public AnalyzedField navigate(@Nonnull T chainedTarget)
			throws NavigationException {
		AnalyzedField field = chainedTarget.getField(fieldName);

		if (field != null) {
			return field;
		}

		throw new NavigationException(String.format(
				"Denomination %s does not have field %s",
				chainedTarget.getDenominationName(), fieldName));
	}

	@Nonnull
	@Override
	public String getStepDescription() {
		return String.format("Field named %s", fieldName);
	}
}
