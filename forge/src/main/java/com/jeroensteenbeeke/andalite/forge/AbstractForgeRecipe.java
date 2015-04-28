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
package com.jeroensteenbeeke.andalite.forge;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Base implementation of ForgeRecipe that enforces the {@code extraSettings}
 * parameter and takes care of name handling
 */
public abstract class AbstractForgeRecipe implements ForgeRecipe {
	private final Map<String, String> extraSettings;

	private final String identifier;

	protected AbstractForgeRecipe(Map<String, String> extraSettings, String name) {
		super();
		this.extraSettings = ImmutableMap.copyOf(extraSettings);
		this.identifier = name;
	}

	@Override
	public String getName() {
		return identifier;
	}

	public Map<String, String> getExtraSettings() {
		return extraSettings;
	}

}
