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
package com.jeroensteenbeeke.andalite.forge.ui.questions.internal;

import java.util.List;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;
import com.jeroensteenbeeke.andalite.forge.ui.actions.Failure;
import com.jeroensteenbeeke.andalite.forge.ui.questions.AbstractQuestion;

public class RecipeSelectionQuestion extends AbstractQuestion<ForgeRecipe> {
	private final List<ForgeRecipe> recipes;

	public RecipeSelectionQuestion(List<ForgeRecipe> recipes) {
		super("Select recipe:");
		this.recipes = recipes;
	}

	public List<ForgeRecipe> getRecipes() {
		return recipes;
	}

	@Override
	public Action onAnswer(ForgeRecipe answer, FeedbackHandler feedbackHandler)
			throws ForgeException {
		return answer.onSelected();
	}

}
