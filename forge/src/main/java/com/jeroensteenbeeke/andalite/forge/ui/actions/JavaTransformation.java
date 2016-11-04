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

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.java.transformation.JavaRecipe;

/**
 * Compoundable action that performs a Java transformation on the given file
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class JavaTransformation extends AbstractCompoundableAction {
	private final File targetFile;

	private final JavaRecipe recipe;

	/**
	 * Create a new Java transformation to apply the given recipe to the target
	 * file
	 * 
	 * @param targetFile
	 *            The file to apply the recipe to
	 * @param recipe
	 *            The recipe to apply
	 */
	public JavaTransformation(@Nonnull File targetFile,
			@Nonnull JavaRecipe recipe) {
		this.targetFile = targetFile;
		this.recipe = recipe;
	}

	@Override
	public ActionResult perform() {
		return recipe.applyTo(targetFile);
	}

}
