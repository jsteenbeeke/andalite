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


public final class Location implements Comparable<Location> {
	private final int start;

	private final int end;

	public Location(int start, int end) {
		if (end < start) {
			throw new IllegalArgumentException("End before start");
		}
		this.start = start;
		this.end = end;
	}

	public int getLength() {
		return end - start;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public Location max(Location other) {
		if (compareTo(other) < 0) {
			return other;
		}

		return this;
	}

	public Location min(Location other) {
		if (compareTo(other) > 0) {
			return other;
		}

		return this;
	}

	public Location intersect(Location other) {
		return new Location(Math.max(start, other.start), Math.min(end, other.end));
	}

	@Override
	public String toString() {
		return String.format("[%d,%d]", start, end);
	}

	@Override
	public int compareTo(Location location) {
		int c = Integer.compare(start, location.start);

		if (c == 0) {
			c = Integer.compare(end, location.end);
		}

		return c;
	}
}
