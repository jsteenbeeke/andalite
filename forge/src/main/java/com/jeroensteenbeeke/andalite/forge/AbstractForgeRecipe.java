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

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.CheckForNull;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;

/**
 * Base implementation of ForgeRecipe that enforces the {@code extraSettings}
 * parameter and takes care of name handling
 */
public abstract class AbstractForgeRecipe implements ForgeRecipe {
	private final Map<String, String> extraSettings;

	private final String identifier;

	protected AbstractForgeRecipe(String name, Map<String, String> extraSettings) {
		super();
		this.extraSettings = ImmutableMap.copyOf(extraSettings);
		this.identifier = name;
	}

	@NotNull
	@Override
	public String getName() {
		return identifier;
	}

	@NotNull
	public final Map<String, String> getExtraSettings() {
		return extraSettings;
	}

	@CheckForNull
	public final String getSetting(String key) {
		return extraSettings.get(key);
	}

	protected final ActionResult ensureSettings(String... keys) {
		Set<String> missing = Sets.newTreeSet();

		for (String key : keys) {
			if (!extraSettings.containsKey(key)) {
				missing.add(key);
			}
		}

		if (!missing.isEmpty()) {
			return ActionResult.error("Missing configuration options: %s",
					Joiner.on(", ").join(missing));
		}

		return ActionResult.ok();
	}

	protected final ActionResult ensureFiles(String type, File path,
			Predicate<AnalyzedSourceFile> condition) {
		if (countFiles(path, condition) == 0) {
			return ActionResult.error("No %s found", type);
		}

		return ActionResult.ok();
	}

	private final int countFiles(File path,
			Predicate<AnalyzedSourceFile> condition) {
		int count = 0;

		return count;
	}
}
