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

import com.jeroensteenbeeke.andalite.transformation.operations.ClassOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.CompilationUnitOperation;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.EnsureImports;
import com.jeroensteenbeeke.andalite.transformation.operations.impl.EnsurePublicClass;

public class Operations {
	private Operations() {

	}

	public static CompilationUnitOperation hasPublicClass() {
		return new EnsurePublicClass();
	}

	public static CompilationUnitOperation imports(String fqdn) {
		return new EnsureImports(fqdn);
	}

	public static ClassOperation hasAnnotation(String annotation) {
		return null;
	}

}
