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
package com.jeroensteenbeeke.andalite.analyzer.types;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class Reference extends AnalyzedType {

	private final AnalyzedType referencedType;
	private final int arrayCount;

	public Reference(@Nonnull final Location location,
			@Nonnull final AnalyzedType referencedType, int arrayCount) {
		super(location);
		this.referencedType = referencedType;
		this.arrayCount = arrayCount;
	}

	public int getArrayCount() {
		return arrayCount;
	}

	public AnalyzedType getReferencedType() {
		return referencedType;
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();

		sb.append(referencedType.toJavaString());
		for (int i = 0; i < arrayCount; i++) {
			sb.append("[]");
		}

		return sb.toString();

	}
}
