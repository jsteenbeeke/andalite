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
package com.jeroensteenbeeke.andalite.xml;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.core.ActionResult;

public class XMLRecipe {
	private static final Logger logger = LoggerFactory
			.getLogger(XMLRecipe.class);

	private final List<XMLRecipeStep<?>> steps;

	public XMLRecipe(List<XMLRecipeStep<?>> steps) {
		super();
		this.steps = steps;
	}

	public ActionResult applyTo(File file) {
		logger.info("Applying transformation ({} steps) to {}", steps.size(),
				file.getName());

		for (XMLRecipeStep<?> step : steps) {

		}

		logger.debug("All steps executed, checking if resulting file can be parsed");

		return ActionResult.error("Not yet implemented");
	}

}
