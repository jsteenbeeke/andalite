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

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedParameter;

public class ParameterNavigation extends ChainedNavigation<AnalyzedMethod, AnalyzedParameter> {
	private final String type;
	
	private final String name;

	public ParameterNavigation(INavigation<AnalyzedMethod> chained,
			String type, String name) {
		super(chained);
		this.type = type;
		this.name = name;
	}
	
	@Override
	public String getStepDescription() {
		return String.format("parameter %s of type %s", name, type);
	}
	
	 
	@Override
	public AnalyzedParameter navigate(AnalyzedMethod chainedTarget)
			throws NavigationException {
		for (AnalyzedParameter analyzedParameter : chainedTarget.getParameters()) {
			if (name.equals(analyzedParameter.getName()) && type.equals(analyzedParameter.getType())) {
				return analyzedParameter;
			}
		}
		
		throw new NavigationException("Method %s has no parameter named %s of type %s", chainedTarget.getName(), name, type);
	}
}
