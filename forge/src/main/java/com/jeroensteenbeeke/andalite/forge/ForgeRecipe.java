package com.jeroensteenbeeke.andalite.forge;

import com.jeroensteenbeeke.andalite.forge.ui.Action;

public interface ForgeRecipe {
	String getName();

	Action onSelected();
}
