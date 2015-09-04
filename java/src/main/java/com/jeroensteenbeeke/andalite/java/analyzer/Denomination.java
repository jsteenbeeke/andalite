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

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.jeroensteenbeeke.andalite.core.Location;

public abstract class Denomination extends AccessModifiable {

	private final String packageName;

	private final String denominationName;

	private final TerminalNode rawNameNode;

	private final Location nameLocation;

	public Denomination(@Nonnull Location location, int modifiers,
			@Nonnull String packageName,
			@Nonnull TerminalNode denominationNameNode) {
		super(location, modifiers);
		this.rawNameNode = denominationNameNode;
		this.nameLocation = Location.from(denominationNameNode);
		this.packageName = packageName;
		this.denominationName = denominationNameNode.getText();
	}

	public TerminalNode getRawNameNode() {
		return rawNameNode;
	}

	public Location getNameLocation() {
		return nameLocation;
	}

	public String getDenominationName() {
		return denominationName;
	}

	public String getPackageName() {
		return packageName;
	}
}
