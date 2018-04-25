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

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.MultipleChoiceQuestion;
import com.jeroensteenbeeke.hyperion.util.TypedResult;

import javax.annotation.Nonnull;
import java.util.List;

public class ScriptedQuestionRenderer implements QuestionRenderer {
	private final List<Object> scriptedAnswers;

	private final FeedbackHandler feedbackHandler;

	private ScriptedQuestionRenderer(List<Object> scriptedAnswers,
			FeedbackHandler feedbackHandler) {
		this.scriptedAnswers = scriptedAnswers;
		this.feedbackHandler = feedbackHandler;
	}

	@Override
	public TypedResult<ForgeRecipe> renderRecipeSelection(
			@Nonnull List<ForgeRecipe> recipeList) {
		Object answer = scriptedAnswers.remove(0);

		if (answer instanceof Integer) {
			return TypedResult.ok(recipeList.get((Integer) answer));
		}

		return TypedResult.fail("Invalid recipe index: %s", answer);
	}

	@Override
	public TypedResult<Answers> renderQuestion(
			@Nonnull Answers answers,
			@Nonnull Question question) {
			if (scriptedAnswers.isEmpty()) {
				return TypedResult.fail(
						"Scenario incorrect, no answers left but questions remaining");
			}
			Object answer = scriptedAnswers.remove(0);
			feedbackHandler.info("Question: %s?", question.getQuestion());
			if (question instanceof MultipleChoiceQuestion) {
				MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;

				mcq.getChoices().forEach(
						c -> feedbackHandler.info("\t\t - %s", c));
			}

			feedbackHandler.info("\tAnswer: %s", answer.toString());

			return TypedResult.ok(answers.plus(question.getKey(), answer));
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
}
