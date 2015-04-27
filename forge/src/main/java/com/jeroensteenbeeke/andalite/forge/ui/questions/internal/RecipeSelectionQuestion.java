package com.jeroensteenbeeke.andalite.forge.ui.questions.internal;

import java.util.List;

import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
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
	public Action onAnswer(ForgeRecipe answer) {
		return answer.onSelected();
	}
	
	
	
	
}
