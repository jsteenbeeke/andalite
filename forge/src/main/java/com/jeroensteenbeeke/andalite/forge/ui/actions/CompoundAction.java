package com.jeroensteenbeeke.andalite.forge.ui.actions;

import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.forge.ui.CompoundableAction;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;

public class CompoundAction implements CompoundableAction {
	private final PerformableAction[] actions;
	
	
	
	CompoundAction(PerformableAction... actions) {
		super();
		this.actions = actions;
	}



	@Override
	public ActionResult perform() {
		for (PerformableAction action : actions) {
			ActionResult result = action.perform();
			if (!result.isOk()) {
				return result;
			}
		}
		
		return ActionResult.ok();
	}
	
	@Override
	public CompoundAction andThen(PerformableAction nextAction) {
		return new CompoundAction(this, nextAction);
	}

}
