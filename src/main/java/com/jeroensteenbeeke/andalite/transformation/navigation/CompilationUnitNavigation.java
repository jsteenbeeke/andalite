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

import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;

public class CompilationUnitNavigation implements
		INavigation<AnalyzedSourceFile> {
	private static final CompilationUnitNavigation instance = new CompilationUnitNavigation();

	private CompilationUnitNavigation() {
	}

	public static CompilationUnitNavigation getInstance() {
		return instance;
	}

	@Override
	public AnalyzedSourceFile navigate(AnalyzedSourceFile file) {
		return file;
	}

	@Override
	public String getDescription() {
		return "Java File";
	}
}
