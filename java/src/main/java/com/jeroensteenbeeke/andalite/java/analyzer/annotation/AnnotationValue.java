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

package com.jeroensteenbeeke.andalite.java.analyzer.annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;

public class AnnotationValue extends BaseValue<AnalyzedAnnotation> {

	public AnnotationValue(@Nonnull Location location, @Nullable String name,
			@Nonnull AnalyzedAnnotation value) {
		super(location, name, value);
	}

	@Override
	public void output(IOutputCallback callback) {
		getValue().output(callback);
	}

	@Override
	public String toJavaString() {
		return getValue().toJavaString();
	}
}
