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
package com.jeroensteenbeeke.andalite.java.analyzer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.*;

import java.util.List;

public abstract class AnalyzedStatement<T extends AnalyzedStatement<T,I>, I extends Enum<I> & IInsertionPoint<T>> extends Locatable implements
		Commentable, IInsertionPointProvider<T, I> {
	private final List<String> comments;

	protected AnalyzedStatement(Location location) {
		super(location);
		this.comments = Lists.newArrayList();
	}

	/**
	 * @non.public
	 */
	@Override
	public void addComment(String comment) {
		this.comments.add(comment.trim());
	}

	@Override
	public List<String> getComments() {
		return ImmutableList.copyOf(comments);
	}

	public abstract String toJavaString();

	@Override
	public void output(IOutputCallback callback) {
		callback.write(toJavaString());
		callback.write(";\n");
	}

	public abstract I getBeforeInsertionPoint();

	public abstract I getAfterInsertionPoint();

}
