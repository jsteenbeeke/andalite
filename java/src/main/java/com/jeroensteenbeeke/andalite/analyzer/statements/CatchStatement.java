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
package com.jeroensteenbeeke.andalite.analyzer.statements;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedType;

public class CatchStatement extends AnalyzedStatement {

	private final BlockStatement block;

	private final String exceptionName;

	private final List<AnalyzedType> exceptionTypes;

	private final List<AnalyzedAnnotation> annotations;

	public CatchStatement(Location location, BlockStatement block,
			String exceptionName) {
		super(location);
		this.block = block;
		this.exceptionName = exceptionName;
		this.exceptionTypes = Lists.newArrayList();
		this.annotations = Lists.newArrayList();
	}

	public void addAnnotation(AnalyzedAnnotation annotation) {
		annotations.add(annotation);
	}

	public List<AnalyzedAnnotation> getAnnotations() {
		return annotations;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void addExceptionType(AnalyzedType type) {
		exceptionTypes.add(type);
	}

	public List<AnalyzedType> getExceptionTypes() {
		return exceptionTypes;
	}

	public BlockStatement getBlock() {
		return block;
	}

	@Override
	public String toJavaString() {
		StringBuilder java = new StringBuilder();

		java.append(" catch (");
		Joiner.on(" ").appendTo(
				java,
				FluentIterable.from(annotations).transform(
						AnalyzedAnnotation.toJavaStringFunction()));
		Joiner.on(" | ").appendTo(
				java,
				FluentIterable.from(exceptionTypes).transform(
						AnalyzedType.toJavaStringFunction()));
		java.append(" ");
		java.append(exceptionName);
		java.append(") ");
		java.append(block.toJavaString());

		return java.toString();
	}
}