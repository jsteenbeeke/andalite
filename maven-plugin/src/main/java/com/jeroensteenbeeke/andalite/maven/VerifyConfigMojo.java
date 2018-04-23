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
package com.jeroensteenbeeke.andalite.maven;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;

@Mojo( name = "verify-config")
public class VerifyConfigMojo extends RecipeMojo {
	

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (recipes == null || recipes.length == 0) {
			throw new MojoFailureException("No recipes defined for this project!");
		}
		
		List<ForgeRecipe> recipeList = determineRecipes();
		
		boolean failed = false;
		
		for (ForgeRecipe recipe: recipeList) {
			getLog().info("Checking configuration for ".concat(recipe.getName()));
			ActionResult actionResult = recipe.checkCorrectlyConfigured();
			if (!actionResult.isOk()) {
				getLog().error(actionResult.getMessage());
				failed = true;
			}
		}
		
		if (failed) {
			throw new MojoFailureException("One or more recipes incorrectly configured");
		}
	
	}

}
