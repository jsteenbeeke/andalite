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
package com.jeroensteenbeeke.andalite.forge.ui.renderer;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.ActionResult;
import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.actions.Failure;

public class ScriptedQuestionRenderer implements QuestionRenderer {
	private final List<Object> answers;

	private final FeedbackHandler feedbackHandler;

	private ScriptedQuestionRenderer(List<Object> answers,
			FeedbackHandler feedbackHandler) {
		this.answers = answers;
		this.feedbackHandler = feedbackHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypedActionResult<Action> renderQuestion(Question<T> question) {
		try {
			Action answer = question.onAnswer((T) answers.remove(0),
					feedbackHandler);

			return TypedActionResult.ok(answer);
		} catch (ForgeException e) {
			return TypedActionResult.fail(e.getMessage());
		}
	}

	public static Builder forAnswers(Object first, Object... rest) {
		List<Object> answers = Lists.newLinkedList();
		answers.add(first);
		for (Object object : rest) {
			answers.add(object);
		}

		return new Builder(answers);
	}

	public static class Builder {
		private final List<Object> answers;

		private Builder(List<Object> answers) {
			this.answers = answers;
		}

		public ScriptedQuestionRenderer withHandler(FeedbackHandler handler) {
			return new ScriptedQuestionRenderer(answers, handler);
		}
	}

	public ActionResult execute(ForgeRecipe recipe) {
		ActionResult result = recipe.checkCorrectlyConfigured();
		if (!result.isOk()) {
			return result;
		}

		try {
			Action next = recipe.onSelected();

			while (next instanceof Question) {
				Question<?> q = (Question<?>) next;
				TypedActionResult<Action> r = renderQuestion(q);
				if (!result.isOk()) {
					return result;
				}

				next = r.getObject();
			}

			if (next instanceof Failure) {
				return ActionResult.error("Forge Recipe returned failure");
			} else if (next instanceof PerformableAction) {
				PerformableAction action = (PerformableAction) next;
				ActionResult r = action.perform();
				if (!r.isOk()) {
					return result;
				}
			}

		} catch (ForgeException e) {
			return ActionResult.error(e.getMessage());
		}

		return ActionResult.ok();
	}
}
