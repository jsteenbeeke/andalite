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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class TryStatement extends BaseStatement {

	private final AnalyzedStatement<?,?> block;

	private final AnalyzedStatement<?,?> finallyStatement;

	private final List<ResourceStatement> resourceStatements;

	private final List<CatchStatement> catchClauses;

	public TryStatement(@Nonnull Location location,
			@Nonnull AnalyzedStatement<?,?> block,
			@Nullable AnalyzedStatement<?,?> finallyStatement) {
		super(location);
		this.block = block;
		this.finallyStatement = finallyStatement;
		this.resourceStatements = Lists.newArrayList();
		this.catchClauses = Lists.newArrayList();
	}

	@Nonnull
	public AnalyzedStatement<?,?> getBlock() {
		return block;
	}

	@CheckForNull
	public AnalyzedStatement<?,?> getFinallyStatement() {
		return finallyStatement;
	}

	public void addResourceStatement(@Nonnull ResourceStatement statement) {
		this.resourceStatements.add(statement);
	}

	@Nonnull
	public List<ResourceStatement> getResourceStatements() {
		return ImmutableList.copyOf(resourceStatements);
	}

	public void addCatchClause(@Nonnull CatchStatement statement) {
		this.catchClauses.add(statement);
	}

	@Nonnull
	public List<CatchStatement> getCatchClauses() {
		return ImmutableList.copyOf(catchClauses);
	}

	@Override
	public String toJavaString() {
		StringBuilder java = new StringBuilder();

		java.append("try ");
		java.append(block.toJavaString());
		Joiner.on(' ').appendTo(
				java,
				catchClauses.stream().map(AnalyzedStatement::toJavaString).collect(Collectors.toList()));
		if (finallyStatement != null) {
			java.append(finallyStatement.toJavaString());
		}

		return java.toString();
	}

}
