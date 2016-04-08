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

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.actions.Failure;
import com.jeroensteenbeeke.andalite.forge.ui.questions.internal.RecipeSelectionQuestion;
import com.jeroensteenbeeke.andalite.maven.ui.MavenQuestionRenderer;

@Mojo(name = "forge", aggregator = true, requiresDirectInvocation = true)
public class ForgeMojo extends RecipeMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (recipes == null || recipes.length == 0) {
			throw new MojoFailureException(
					"No recipes defined for this project!");
		}

		List<ForgeRecipe> recipeList = determineRecipes();

		MavenQuestionRenderer renderer = new MavenQuestionRenderer();
		Action next = new RecipeSelectionQuestion(recipeList);

		while (next instanceof Question) {
			getLog().debug(String.format("Next action: %s", next.toString()));
			
			Question<?> q = (Question<?>) next;
			TypedActionResult<Action> result = renderer.renderQuestion(q);
			if (!result.isOk()) {
				throw new MojoFailureException(result.getMessage());
			}

			next = result.getObject();
		}
		
		getLog().debug(String.format("Next action: %s", next.toString()));

		if (next instanceof Failure) {
			throw new MojoFailureException("Forge Recipe returned failure");
		} else if (next instanceof PerformableAction) {
			PerformableAction action = (PerformableAction) next;
			ActionResult result = action.perform();
			if (!result.isOk()) {
				throw new MojoFailureException(result.getMessage());
			}
			getLog().info("Recipe completed, continuing");
			execute();
		} else {
			getLog().info("Forge completed");
		}
	}

}
