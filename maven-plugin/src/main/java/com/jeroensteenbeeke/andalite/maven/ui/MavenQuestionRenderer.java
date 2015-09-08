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
package com.jeroensteenbeeke.andalite.maven.ui;

import java.io.File;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.core.TypedActionResult;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.questions.FileSelectQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.MultipleChoiceQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.SimpleQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.YesNoQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.internal.RecipeSelectionQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.renderer.QuestionRenderer;

public class MavenQuestionRenderer implements QuestionRenderer, FeedbackHandler {
	private static final int YES = 1;

	private static final int NO = 2;

	private static final Logger log = LoggerFactory
			.getLogger(MavenQuestionRenderer.class);

	@Override
	public <T> TypedActionResult<Action> renderQuestion(
			@Nonnull Question<T> question) {
		try {
			if (question instanceof SimpleQuestion) {
				return renderSimpleQuestion((SimpleQuestion) question);
			}
			if (question instanceof YesNoQuestion) {
				return renderYesNoQuestion((YesNoQuestion) question);
			}
			if (question instanceof MultipleChoiceQuestion) {
				return renderMultipleChoiceQuestion((MultipleChoiceQuestion) question);
			}
			if (question instanceof FileSelectQuestion) {
				return renderFileSelectQuestion((FileSelectQuestion) question);
			}
			if (question instanceof RecipeSelectionQuestion) {
				return renderRecipeSelectQuestion((RecipeSelectionQuestion) question);
			}
		} catch (ForgeException e) {
			return TypedActionResult.fail(e.getMessage());
		}

		return TypedActionResult.fail("Unknown question type: %s", question
				.getClass().getName());
	}

	private TypedActionResult<Action> renderRecipeSelectQuestion(
			RecipeSelectionQuestion question) throws ForgeException {
		TypedActionResult<Integer> response = TypedActionResult.fail("");

		final int exit = question.getRecipes().size() + 1;

		while (!response.isOk()) {
			String message = response.getMessage();
			if (message != null && !message.isEmpty()) {
				System.out.println(message);
				System.out.println();
			}
			System.out.println(question.getQuestion());
			int i = 0;
			for (ForgeRecipe choice : question.getRecipes()) {
				System.out.print("\t[");
				System.out.print(++i);
				System.out.print("] ");
				System.out.println(choice.getName());

			}

			System.out.print("\t[");
			System.out.print(++i);
			System.out.println("] Done");

			response = getNumberResponse(1, i + 1);
		}

		if (response.getObject().intValue() == exit) {
			return TypedActionResult.ok(Completed.get());
		}

		return TypedActionResult.ok(question.onAnswer(question.getRecipes()
				.get(response.getObject() - 1), this));
	}

	private TypedActionResult<Action> renderMultipleChoiceQuestion(
			MultipleChoiceQuestion question) throws ForgeException {
		TypedActionResult<Integer> response = TypedActionResult.fail("");

		while (!response.isOk()) {
			String message = response.getMessage();
			if (message != null && !message.isEmpty()) {
				System.out.println(message);
				System.out.println();
			}
			System.out.println(question.getQuestion());
			int i = 0;
			for (String choice : question.getChoices()) {
				System.out.print("\t[");
				System.out.print(++i);
				System.out.print("] ");
				System.out.println(choice);

			}

			response = getNumberResponse(1, i);
		}

		return TypedActionResult.ok(question.onAnswer(question.getChoices()
				.get(response.getObject() - 1), this));
	}

	private TypedActionResult<Action> renderFileSelectQuestion(
			FileSelectQuestion question) throws ForgeException {
		TypedActionResult<Integer> response = TypedActionResult.fail("");

		while (!response.isOk()) {
			String message = response.getMessage();
			if (message != null && !message.isEmpty()) {
				System.out.println(message);
				System.out.println();
			}
			System.out.println(question.getQuestion());
			int i = 0;
			for (File choice : question.getChoices()) {
				System.out.print("\t[");
				System.out.print(++i);
				System.out.print("] ");
				System.out.println(choice.getPath());

			}

			response = getNumberResponse(1, i);
		}

		return TypedActionResult.ok(question.onAnswer(question.getChoices()
				.get(response.getObject() - 1), this));
	}

	private TypedActionResult<Action> renderYesNoQuestion(YesNoQuestion question)
			throws ForgeException {
		TypedActionResult<Integer> response = TypedActionResult.fail("");

		while (!response.isOk()) {
			String message = response.getMessage();
			if (message != null && !message.isEmpty()) {
				System.out.println(message);
				System.out.println();
			}
			System.out.println(question.getQuestion());
			System.out.println("\t[1] Yes");
			System.out.println("\t[2] No");
			response = getNumberResponse(YES, NO);
		}

		return TypedActionResult.ok(question.onAnswer(response.getObject()
				.equals(YES), this));
	}

	private TypedActionResult<Action> renderSimpleQuestion(
			SimpleQuestion question) throws ForgeException {
		System.out.println(question.getQuestion());
		String answer = System.console().readLine();
		return TypedActionResult.ok(question.onAnswer(answer, this));
	}

	private TypedActionResult<Integer> getNumberResponse(int low, int high) {
		String input = System.console().readLine();

		try {
			int number = Integer.parseInt(input);

			if (number < low || number > high) {
				return TypedActionResult.fail("Invalid input: %d", number);
			}

			return TypedActionResult.ok(number);
		} catch (NumberFormatException nfe) {
			return TypedActionResult.fail(nfe.getMessage());
		}

	}

	@Override
	public void error(String message, Object... args) {
		log.error(formatMessage(message, args));
	}

	@Override
	public void info(String message, Object... args) {
		log.info(formatMessage(message, args));
	}

	@Override
	public void warning(String message, Object... args) {
		log.warn(formatMessage(message, args));
	}

	private String formatMessage(String message, Object[] args) {
		if (args.length == 0) {
			return message;
		}
		return String.format(message, args);
	}

}
