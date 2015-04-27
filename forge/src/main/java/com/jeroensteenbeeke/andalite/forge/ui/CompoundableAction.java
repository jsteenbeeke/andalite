package com.jeroensteenbeeke.andalite.forge.ui;

import com.jeroensteenbeeke.andalite.forge.ui.actions.CompoundAction;

/**
 * Syntactic sugar interface
 */
public interface CompoundableAction extends PerformableAction {
	CompoundAction andThen(PerformableAction nextAction);
}
