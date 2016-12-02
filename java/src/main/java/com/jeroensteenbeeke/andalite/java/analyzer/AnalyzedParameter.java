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

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of a method/constructor parameter
 * 
 * @author Jeroen Steenbeeke
 */
public class AnalyzedParameter extends Annotatable {
	private final String type;

	private final String name;

	/**
	 * Create a new AnalyzedParameter
	 * 
	 * @param location
	 *            The location of the parameter
	 * @param type
	 *            The type of the parameter
	 * @param name
	 *            The name of the parameter
	 */
	AnalyzedParameter(@Nonnull Location location, @Nonnull String type,
			@Nonnull String name) {
		super(location);
		this.type = type;
		this.name = name;
	}

	/**
	 * Get name of the parameter
	 * 
	 * @return The name of the parameter
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * Get the type of the parameter
	 * 
	 * @return The type of the parameter (String representation)
	 */
	@Nonnull
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
}
