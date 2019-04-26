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
package com.jeroensteenbeeke.andalite.java.analyzer.statements;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedAnnotation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedType;
import com.jeroensteenbeeke.andalite.java.analyzer.IAnnotationAddable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class CatchStatement extends BaseStatement implements IAnnotationAddable<CatchStatement> {

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

	public CatchStatement addAnnotation(@Nonnull AnalyzedAnnotation annotation) {
		annotations.add(annotation);
		return this;
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
				annotations.stream().map(AnalyzedAnnotation::toJavaString).collect(Collectors.toList()));
		Joiner.on(" | ").appendTo(
				java,
				exceptionTypes.stream().map(AnalyzedType::toJavaString).collect(Collectors.toList()));
		java.append(" ");
		java.append(exceptionName);
		java.append(") ");
		java.append(block.toJavaString());

		return java.toString();
	}
}
