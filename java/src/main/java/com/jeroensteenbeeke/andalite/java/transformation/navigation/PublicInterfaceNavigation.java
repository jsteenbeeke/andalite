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

import com.jeroensteenbeeke.andalite.core.exceptions.NavigationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;

public class PublicInterfaceNavigation implements
		IJavaNavigation<AnalyzedInterface> {

	@Override
	public AnalyzedInterface navigate(AnalyzedSourceFile file)
			throws NavigationException {
		for (AnalyzedInterface analyzedInterface : file.getInterfaces()) {
			if (analyzedInterface.getAccessModifier() == AccessModifier.PUBLIC) {
				return analyzedInterface;
			}
		}

		throw new NavigationException("Source file has no public interface");
	}

	@Override
	public String getDescription() {
		return "Public interface";
	}

}
