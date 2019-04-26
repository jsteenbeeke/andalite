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

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IInsertionPointProvider;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedConstructor;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedMethod;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;
import com.jeroensteenbeeke.andalite.java.analyzer.IBodyContainer;

/**
 * Represents a statement that is a block of statements, such as one generally
 * following an if-statement
 */
public class BlockStatement extends AnalyzedStatement<BlockStatement, BlockStatement.BlockStatementInsertionPoint> implements IBodyContainer<BlockStatement, BlockStatement.BlockStatementInsertionPoint> {
	private List<AnalyzedStatement<?,?>> statements;

	/**
	 * Creates a new block statement
	 * 
	 * THIS METHOD IS NOT PART OF THE PUBLIC API! DO NOT USE IT UNLESS YOU KNOW
	 * WHAT YOU ARE DOING
	 * 
	 * @param location
	 *            The location of the statement
	 */
	public BlockStatement(@Nonnull Location location) {
		super(location);
		this.statements = Lists.newArrayList();
	}

	/**
	 * Block statements are not abstract, returns {@code false}
	 */
	@Override
	public boolean isAbstract() {
		return false;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE PUBLIC API! DO NOT USE IT UNLESS YOU KNOW
	 * WHAT YOU ARE DOING
	 * 
	 * @param statement
	 *            The statement to add to this block
	 */
	public void addStatement(@Nonnull AnalyzedStatement<?,?> statement) {
		this.statements.add(statement);
	}

	/**
	 * Returns the list of contained statements
	 */
	@Override
	@Nonnull
	public List<AnalyzedStatement<?,?>> getStatements() {
		return ImmutableList.copyOf(statements);
	}

	/**
	 * Creates a simplified Java representation of the analyzed block statement
	 */
	@Override
	@Nonnull
	public final String toJavaString() {
		StringBuilder block = new StringBuilder();

		block.append("{\n");
		for (AnalyzedStatement<?,?> statement : getStatements()) {
			block.append(statement.toJavaString());
		}

		block.append("}\n");

		return block.toString();
	}

	@Override
	public BlockStatementInsertionPoint getBeforeInsertionPoint() {
		return BlockStatementInsertionPoint.BEFORE;
	}

	@Override
	public BlockStatementInsertionPoint getAfterInsertionPoint() {
		return BlockStatementInsertionPoint.AFTER;
	}

	@Override
	public BlockStatementInsertionPoint getStatementInsertionPoint() {
		return BlockStatementInsertionPoint.BEFORE_RETURN_STATEMENT;
	}

	public enum BlockStatementInsertionPoint implements IInsertionPoint<BlockStatement> {
		BEFORE {
			@Override
			public int position(BlockStatement container) {
				return container.getLocation().getStart();
			}
		}, AFTER {
			@Override
			public int position(BlockStatement container) {
				return container.getLocation().getEnd();
			}
		},
		START_OF_BODY {
			@Override
			public int position(BlockStatement container) {
				return container.getLocation().getStart() + 1;
			}

		},
		END_OF_BODY {
			@Override
			public int position(BlockStatement container) {
				return container.getLocation().getEnd() - 1;
			}
		},
		BEFORE_RETURN_STATEMENT {
			@Override
			public int position(BlockStatement container) {
				List<AnalyzedStatement<?,?>> returnStatements = container
					.getStatements()
					.stream()
					.filter(s -> s instanceof ReturnStatement)
					.collect(Collectors.toList());

				// No return statement: assume implicit empty return
				if (returnStatements.isEmpty()) {
					return END_OF_BODY.position(container);
				}

				return returnStatements.get(returnStatements.size()-1).getLocation().getEnd();
			}
		}

	}
}
