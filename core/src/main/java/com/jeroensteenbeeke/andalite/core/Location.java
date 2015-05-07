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

import com.github.antlrjavaparser.api.Node;

public final class Location {
	private final int start;

	private final int end;

	public Location(@Nonnull int start, @Nonnull int end) {
		super();
		this.start = start;
		this.end = end;
	}

	@Nonnull
	public int getStart() {
		return start;
	}

	@Nonnull
	public int getEnd() {
		return end;
	}

	@Nonnull
	public static Location from(@Nonnull Node node) {
		return new Location(node.getBeginIndex(), node.getEndIndex() + 1);
	}
}
