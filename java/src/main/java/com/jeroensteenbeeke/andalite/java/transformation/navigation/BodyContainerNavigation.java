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

import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;

public class BodyContainerNavigation<T extends IBodyContainer<T,I>, I extends Enum<I> & IInsertionPoint<T>> extends
		ChainedNavigation<T, T> {

	public BodyContainerNavigation(IJavaNavigation<T> chained) {
		super(chained);
	}

	@Override
	public T navigate(T chainedTarget) throws NavigationException {

		return chainedTarget;
	}

	@Override
	public String getStepDescription() {

		return "into the body";
	}
}
