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
package com.jeroensteenbeeke.andalite.forge.ui.renderer;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;
import com.jeroensteenbeeke.andalite.forge.ui.PerformableAction;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.actions.Failure;
import com.jeroensteenbeeke.andalite.forge.ui.questions.AbstractQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.MultipleChoiceQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.NoQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.QuestionTemplate;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.lux.ActionResult;
import com.jeroensteenbeeke.lux.TypedResult;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
		@NotNull List<ForgeRecipe> recipeList) {
		Object answer = scriptedAnswers.remove(0);

		if (answer instanceof Integer) {
			return TypedResult.ok(recipeList.get((Integer) answer));
		}

		return TypedResult.fail("Invalid recipe index: %s", answer);
	}

	@Override
	public TypedResult<Answers> renderQuestion(
		@NotNull Answers answers,
		@NotNull Question question) {
		if (scriptedAnswers.isEmpty()) {
			return TypedResult.fail(
				"Scenario incorrect, no answers left but questions remaining. Next question: "+ question.getQuestion());
		}

		if (question instanceof NoQuestion) {
			return TypedResult.ok(answers);
		}

		Object answer = scriptedAnswers.remove(0);
		feedbackHandler.info("Question: %s", question.getQuestion());
		if (question instanceof MultipleChoiceQuestion mcq) {

		    mcq.getChoices().forEach(
				c -> feedbackHandler.info("\t\t - %s", c));
		}

		feedbackHandler.info("\tAnswer: %s", answer.toString());

		if (question instanceof AbstractQuestion aq) {
		    if (!aq.isValidAnswer(answer)) {
				if (answer instanceof File) {
					TypedResult<AnalyzedSourceFile> file = new ClassAnalyzer((File) answer).analyze()
						.filter(aq::isValidAnswer, String.format("%s is not a valid answer", answer));

					if (!file.isOk()) {
						return file.map(f -> answers);
					}
				} else if (answer instanceof AnalyzedSourceFile) {
					AnalyzedSourceFile source = (AnalyzedSourceFile) answer;
					if (!aq.isValidAnswer(source.getOriginalFile())) {
						return TypedResult.fail("%s is not a valid answer", answer);
					}
				} else {
					return TypedResult.fail("%s is not a valid answer", answer);

				}

			}
		}

		return TypedResult.ok(answers.plus(question.getKey(), answer));
	}

	public static Builder forAnswers(Object first, Object... rest) {
		List<Object> answers = Lists.newLinkedList();
		answers.add(first);
	answers.addAll(Arrays.asList(rest));

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
		ActionResult rv = recipe.checkCorrectlyConfigured();
		if (!rv.isOk()) {
			return rv;
		}

		Answers answers = Answers.zero();
		Action action;
		try {
			List<QuestionTemplate<?, ?>> questions = new ArrayList<>();
			questions.add(recipe.getInitialQuestion());

			while (!questions.isEmpty()) {
				QuestionTemplate<?, ?> next = questions.remove(0);

				TypedResult<Answers> result = renderQuestion(answers, next.toQuestion(answers));
				if (!result.isOk()) {
					return result.asSimpleResult();
				} else {
					answers = result.getObject();
				}

				List<QuestionTemplate<?, ?>> followUp = next.getFollowUpQuestions(answers);
				for (int i = followUp.size() - 1; i >= 0; i--) {
					questions.add(0, followUp.get(i));
				}
			}

			action = recipe.createAction(answers);
		} catch (Exception e) {
			return ActionResult.error(e.getMessage());
		}

		if (action instanceof PerformableAction) {
			return ((PerformableAction) action).perform();
		} else {
			return ActionResult.error("Resulting action %s is not performable", action.getClass().getName());
		}
	}
}
