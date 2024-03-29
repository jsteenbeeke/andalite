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
package com.jeroensteenbeeke.andalite.forge.ui.actions;

import java.io.File;

import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;
import com.jeroensteenbeeke.lux.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaTransformation extends AbstractCompoundableAction {
	private static final Logger log = LoggerFactory.getLogger(JavaTransformation.class);

	private final File targetFile;

	private final JavaRecipe recipe;

	public JavaTransformation(File targetFile, JavaRecipe recipe) {
		this.targetFile = targetFile;
		this.recipe = recipe;
	}

	@Override
	public ActionResult perform() {
		log.info("Transforming {}", targetFile.getPath());

	    return recipe.applyTo(targetFile).asSimpleResult();
	}

}
