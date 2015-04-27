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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.actions.Failure;
import com.jeroensteenbeeke.andalite.forge.ui.questions.internal.RecipeSelectionQuestion;
import com.jeroensteenbeeke.andalite.maven.ui.MavenQuestionRenderer;

/**
 * Executes a forge recipe
 * 
 * @goal forge
 */

public class ForgeMojo extends AbstractMojo {
	@Parameter
	private String[] recipes;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		List<ForgeRecipe> recipeList = Lists.newArrayList();
		
		for (String className: recipes) {
			try {
				Class<?> recipeClass = Class.forName(className);
				
				if (ForgeRecipe.class.isAssignableFrom(recipeClass)) {
					recipeList.add((ForgeRecipe) recipeClass.newInstance());
				} else {
					getLog().error("Class "+ recipeClass.getName() +" does not implement the ForgeRecipe interface");
				}
			} catch (ClassNotFoundException e) {
				getLog().error("Recipe not found: "+ className);
				getLog().error(e);
			} catch (InstantiationException e) {
				getLog().error("Could not instantiate recipe: "+ className);
				getLog().error(e);
			} catch (IllegalAccessException e) {
				getLog().error(e);
			}
		}
		
		MavenQuestionRenderer renderer = new MavenQuestionRenderer();
		Action next = new RecipeSelectionQuestion(recipeList);
		
		while (next instanceof Question) {
			Question<?> q = (Question<?>) next;
			TypedActionResult<Action> result = renderer.renderQuestion(q);
			if (!result.isOk()) {
				throw new MojoFailureException(result.getMessage());
			}
			
			next = result.getObject();
		}
		
		if (next instanceof Failure) {
			throw new MojoFailureException("Forge Recipe returned failure");
		} else if (next instanceof PerformableAction) {
			PerformableAction action = (PerformableAction) next;
			ActionResult result = action.perform();
			if (!result.isOk()) {
				throw new MojoFailureException(result.getMessage());
			}
		}
		
	
	}

}
