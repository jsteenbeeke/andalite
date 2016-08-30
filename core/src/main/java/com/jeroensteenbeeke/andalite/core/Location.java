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

package com.jeroensteenbeeke.andalite.core;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.antlrjavaparser.api.Node;

/**
 * A location within a source file, which is basically a range of character
 * indices
 * 
 * @author Jeroen Steenbeeke
 */
public final class Location {
	private final int start;

	private final int end;

	/**
	 * Create a new location with the indicated start and end index
	 * 
	 * @param start
	 *            The start index
	 * @param end
	 *            The end index, should not be less than the start index
	 */
	public Location(int start, int end) {
		if (end < start) {
			throw new IllegalArgumentException("End before start");
		}
		this.start = start;
		this.end = end;
	}

	/**
	 * Indicates the number of characters this location spans
	 * 
	 * @return The number of characters contained by this locatio
	 */
	@Nonnull
	public int getLength() {
		return end - start;
	}

	/**
	 * Gets the lower bound index of this location
	 * 
	 * @return A 1-based source code character index
	 */
	@Nonnull
	public int getStart() {
		return start;
	}

	/**
	 * Gets the upper bound index of this location
	 * 
	 * @return A 1-based source code character index
	 */
	@Nonnull
	public int getEnd() {
		return end;
	}

	/**
	 * Gets a location based on an AST node from the Java parser
	 * 
	 * @param node
	 *            The node to use as a basis
	 * @return A Location representing the position of the given node
	 */
	@Nonnull
	public static Location from(@Nonnull Node node) {
		return new Location(node.getBeginIndex(), node.getEndIndex() + 1);
	}

	/**
	 * Gets a location based on an AST node from the Java parser
	 * 
	 * @param node
	 *            The node to use as a basis
	 * @return A Location representing the position of the given node
	 */
	@Nonnull
	public static Location from(@Nonnull TerminalNode node) {
		Token symbol = node.getSymbol();

		return new Location(symbol.getStartIndex(), symbol.getStopIndex() + 1);
	}

	@Override
	public String toString() {
		return String.format("[%d,%d]", start, end);
	}
}
