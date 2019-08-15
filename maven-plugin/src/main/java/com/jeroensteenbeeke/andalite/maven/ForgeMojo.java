/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.maven;

import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.actions.Failure;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.QuestionTemplate;
import com.jeroensteenbeeke.andalite.forge.ui.renderer.QuestionRenderer;
import com.jeroensteenbeeke.andalite.maven.ui.FileInputRenderer;
import com.jeroensteenbeeke.andalite.maven.ui.MavenQuestionRenderer;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.lux.TypedResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Mojo(name = "forge", aggregator = true, requiresDirectInvocation = true)
public class ForgeMojo extends RecipeMojo {
	@Parameter(required = false, property = "forge.input")
	private File inputFile;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (recipes == null || recipes.length == 0) {
			throw new MojoFailureException(
				"No recipes defined for this project!");
		}

		List<ForgeRecipe> recipeList = determineRecipes();


		QuestionRenderer renderer;
		try {
			renderer = inputFile != null ? new FileInputRenderer(inputFile)
				: new MavenQuestionRenderer();
		} catch (IOException e) {
			throw new MojoFailureException("Could not read input file", e);
		}

		TypedResult<ForgeRecipe> selection = renderer.renderRecipeSelection(recipeList);

		if (!selection.isOk()) {
			throw new MojoFailureException(selection.getMessage());
		}

		ForgeRecipe recipe = selection.getObject();

		if (recipe instanceof ForgeRecipe.ExitRecipe) {
			getLog().info("Exit selected");
			return;
		}

		Answers answers = Answers.zero();
		Action action;
		try {
			List<QuestionTemplate<?, ?>> questions = new ArrayList<>();
			questions.add(recipe.getInitialQuestion());

			while (!questions.isEmpty()) {
				QuestionTemplate<?, ?> next = questions.remove(0);

				TypedResult<Answers> result = renderer.renderQuestion(answers, next.toQuestion(answers));
				if (!result.isOk()) {
					throw new MojoFailureException(result.getMessage());
				} else {
					answers = result.getObject();
				}

				List<QuestionTemplate<?, ?>> followUp = next.getFollowUpQuestions(answers);
				for (int i = followUp.size() - 1; i >= 0; i--) {
					questions.add(0, followUp.get(i));
				}			}

			action = recipe.createAction(answers);
		} catch (Exception e) {
			throw new MojoFailureException(e.getMessage(), e);
		}

		getLog().debug(String.format("Next action: %s", action.toString()));

		if (action instanceof Failure) {
			Failure failure = (Failure) action;
			throw new MojoFailureException(
				String.format("Forge Recipe returned failure: %s", failure.getReason()));
		} else if (action instanceof PerformableAction) {
			PerformableAction performableAction = (PerformableAction) action;
			ActionResult result = performableAction.perform();
			if (!result.isOk()) {
				throw new MojoFailureException(result.getMessage());
			}
			if (inputFile != null) {
				getLog().info("Scripted mode, not continuing");
			} else {
				getLog().info("Recipe completed, continuing");
				execute();
			}
		} else {
			getLog().info("Forge completed");
		}
	}

}
