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
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.andalite.core.ActionResult;

/**
 * Base implementation of ForgeRecipe that enforces the {@code extraSettings}
 * parameter and takes care of name handling
 */
public abstract class AbstractForgeRecipe implements ForgeRecipe {
	private final Map<String, String> settings;

	private final String name;

	/**
	 * Create a new AbstractForgeRecipe with the given name and settings
	 * 
	 * @param name
	 *            The name of the recipe
	 * @param settings
	 *            The settings defined for all recipes
	 */
	protected AbstractForgeRecipe(@Nonnull String name,
			@Nonnull Map<String, String> settings) {
		this.settings = ImmutableMap.copyOf(settings);
		this.name = name;
	}

	@Override
	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * Return all recipe settings
	 * 
	 * @return An immutable map containing all recipe settings
	 */
	@Nonnull
	public final Map<String, String> getSettings() {
		return settings;
	}

	/**
	 * Get the given setting
	 * 
	 * @param key
	 *            The key for the settings
	 * @return The setting associated with the key, if it exists
	 */
	@CheckForNull
	public final String getSetting(@Nonnull String key) {
		return settings.get(key);
	}

	/**
	 * Convenience method for determining if required settings are present
	 * 
	 * @param keys
	 *            The settings that should be present
	 * @return An ActionResult that indicates success, or if not: a list of keys
	 *         that were not present
	 */
	@Nonnull
	protected final ActionResult ensureSettings(@Nonnull String... keys) {
		Set<String> missing = Sets.newTreeSet();

		for (String key : keys) {
			if (!settings.containsKey(key)) {
				missing.add(key);
			}
		}

		if (!missing.isEmpty()) {
			return ActionResult.error("Missing configuration options: %s",
					Joiner.on(", ").join(missing));
		}

		return ActionResult.ok();
	}
}
