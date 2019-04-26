/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeroensteenbeeke.andalite.java.analyzer;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.*;

import java.util.stream.Collectors;

public class AnalyzedParameter extends Annotatable<AnalyzedParameter, AnalyzedParameter.ParameterInsertionPoint> implements IOutputable {
	private final String type;

	private final String name;

	public AnalyzedParameter(@Nonnull Location location, @Nonnull String type,
							 @Nonnull String name) {
		super(location);
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Override
	public boolean isPrintNewlineAfterAnnotation() {
		return false;
	}

	@Override
	public void onOutput(IOutputCallback callback) {
		callback.write(type);
		callback.write(" ");
		callback.write(name);
	}


	public String toJavaString() {
		return String.format("%s%s %s", getAnnotations()
			.stream()
			.map(AnalyzedAnnotation::toJavaString)
			.collect(Collectors
						 .joining(" ", "", " ")), type, name);
	}

	@Override
	public ParameterInsertionPoint getAnnotationInsertPoint() {
		return ParameterInsertionPoint.BEFORE_PARAMETER;
	}

	public enum ParameterInsertionPoint implements IInsertionPoint<AnalyzedParameter> {
		BEFORE_PARAMETER {
			@Override
			public int position(AnalyzedParameter container) {
				return container.getLocation().getStart();
			}
		}
	}
}
