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

import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;

public abstract class ChainedNavigation<From extends ILocatable, To extends ILocatable>
		implements IJavaNavigation<To> {
	private final IJavaNavigation<From> chained;

	protected ChainedNavigation(IJavaNavigation<From> chained) {
		super();
		this.chained = chained;
	}

	@Override
	public final To navigate(AnalyzedSourceFile file)
			throws NavigationException {
		From chainedTarget = chained.navigate(file);

		return navigate(chainedTarget);
	}

	public abstract To navigate(From chainedTarget) throws NavigationException;

	@Override
	public final String getDescription() {
		return String.format("%s,\n\tthen %s", chained.getDescription(),
				getStepDescription());
	}

	public abstract String getStepDescription();
}
