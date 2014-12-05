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

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedClass;

public class InnerClassNavigation extends
		ChainedNavigation<AnalyzedClass, AnalyzedClass> {
	private final String className;

	public InnerClassNavigation(Navigation<AnalyzedClass> chained,
			String className) {
		super(chained);
		this.className = className;
	}

	@Override
	public AnalyzedClass navigate(AnalyzedClass chainedTarget)
			throws NavigationException {
		if (chainedTarget.hasInnerClass(className)) {
			return chainedTarget.getInnerClass(className);
		}

		throw new NavigationException(String.format(
				"Class %s has no inner class named %s",
				chainedTarget.getClassName(), className));
	}

	@Override
	public String getDescription() {

		return String.format("Go to inner class %s", className);
	}
}
