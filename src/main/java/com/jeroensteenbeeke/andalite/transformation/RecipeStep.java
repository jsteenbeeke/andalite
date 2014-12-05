package com.jeroensteenbeeke.andalite.transformation;

import java.util.List;

import com.jeroensteenbeeke.andalite.ActionResult;
import com.jeroensteenbeeke.andalite.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.analyzer.Locatable;
import com.jeroensteenbeeke.andalite.transformation.navigation.Navigation;
import com.jeroensteenbeeke.andalite.transformation.navigation.NavigationException;
import com.jeroensteenbeeke.andalite.transformation.operations.Operation;

public class RecipeStep<T extends Locatable> {
	private final Navigation<T> navigation;

	private final Operation<T> operation;

	RecipeStep(Navigation<T> navigation, Operation<T> operation) {
		super();
		this.navigation = navigation;
		this.operation = operation;
	}

	public ActionResult perform(AnalyzedSourceFile file) {
		T target;
		try {
			target = navigation.navigate(file);
		} catch (NavigationException e) {
			return ActionResult.error(e.getMessage());
		}
		if (target != null) {
			List<Transformation> transformations = operation.perform(target);
			for (Transformation transformation : transformations) {
				ActionResult result = transformation.applyTo(file
						.getOriginalFile());
				if (!result.isOk()) {
					return result;
				}
			}

			return ActionResult.ok();
		} else {
			return ActionResult.error("Could not locate %s",
					navigation.getDescription());
		}
	}
}
