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

import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.questions.*;
import com.jeroensteenbeeke.andalite.forge.ui.renderer.QuestionRenderer;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.lux.TypedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class MavenQuestionRenderer implements QuestionRenderer, FeedbackHandler {
	private static final int YES = 1;

	private static final int NO = 2;

	private static final Logger log = LoggerFactory
			.getLogger(MavenQuestionRenderer.class);

	@Override
	public TypedResult<Answers> renderQuestion(
			@Nonnull Answers answers,
			@Nonnull Question question) {
		if (question instanceof SimpleQuestion) {
			return renderSimpleQuestion((SimpleQuestion) question)
					.map(o -> answers.plus(question.getKey(), o));
		}
		if (question instanceof YesNoQuestion) {
			return renderYesNoQuestion((YesNoQuestion) question)
					.map(o -> answers.plus(question.getKey(), o));
		}
		if (question instanceof MultipleChoiceQuestion) {
			return renderMultipleChoiceQuestion((MultipleChoiceQuestion) question)
					.map(o -> answers.plus(question.getKey(), o));
		}
		if (question instanceof FileSelectQuestion) {
			return renderFileSelectQuestion((FileSelectQuestion) question)
					.map(o -> answers.plus(question.getKey(), o));
		}

		if (question instanceof SourceFileSelectQuestion) {
			return renderSourceFileSelectQuestion((SourceFileSelectQuestion) question)
				.map(o -> answers.plus(question.getKey(), o));
		}
		return TypedResult.fail("Unknown question type: %s", question.getClass());
	}

	@Override
	public TypedResult<ForgeRecipe> renderRecipeSelection(
			@Nonnull
					List<ForgeRecipe> recipeList) {
		TypedResult<Integer> response = createPlaceholder();

		while (!response.isOk()) {
			String message = response.getMessage();
			if (message != null && !message.isEmpty()) {
				System.out.println(message);
				System.out.println();
			}
			System.out.println("Select Recipe");
			int i = 0;
			for (ForgeRecipe recipe : recipeList) {
				System.out.print("\t[");
				System.out.print(++i);
				System.out.print("] ");
				System.out.println(recipe.getName());

			}

			System.out.print("\t[");
			System.out.print(++i);
			System.out.println("] Exit forge");

			response = getNumberResponse(1, i);
		}


		int selection = response.getObject() - 1;

		if (selection >= recipeList.size()) {
			return TypedResult.ok(ForgeRecipe.ExitRecipe.instance());
		}

		ForgeRecipe answer = recipeList.get(selection);

		return TypedResult.ok(answer);	}

	private TypedResult<String> renderMultipleChoiceQuestion(MultipleChoiceQuestion question) {
		TypedResult<Integer> response = createPlaceholder();

		String answer = "";

		while (!question.isValidAnswer(answer)) {
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

			answer = question.getChoices().get(response.getObject() - 1);
		}
		return TypedResult.ok(answer);
	}

	private <T> TypedResult<T> createPlaceholder() {
		return TypedResult.fail("-");
	}

	private TypedResult<File> renderFileSelectQuestion(
			FileSelectQuestion question) {
		TypedResult<Integer> response = createPlaceholder();

		File answer = null;

		while (!question.isValidAnswer(answer)) {
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

			answer = question.getChoices().get(response.getObject() - 1);
		}

		return TypedResult.ok(answer);
	}

	private TypedResult<AnalyzedSourceFile> renderSourceFileSelectQuestion(
		SourceFileSelectQuestion question) {
		TypedResult<Integer> response = createPlaceholder();

		AnalyzedSourceFile answer = null;

		while (!question.isValidAnswer(answer)) {
			while (!response.isOk()) {
				String message = response.getMessage();
				if (message != null && !message.isEmpty()) {
					System.out.println(message);
					System.out.println();
				}
				System.out.println(question.getQuestion());
				int i = 0;
				for (AnalyzedSourceFile choice : question.getChoices()) {
					System.out.print("\t[");
					System.out.print(++i);
					System.out.print("] ");
					System.out.println(choice.getFullyQualifiedName());
				}

				response = getNumberResponse(1, i);
			}

			answer = question.getChoices().get(response.getObject() - 1);
		}

		return TypedResult.ok(answer);
	}

	private TypedResult<Boolean> renderYesNoQuestion(YesNoQuestion question) {
		TypedResult<Integer> response = createPlaceholder();

		Boolean answer = null;

		while (!question.isValidAnswer(answer)) {

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

			answer = response.getObject().equals(YES);
		}

		return TypedResult.ok(answer);
	}

	private TypedResult<String> renderSimpleQuestion(
			SimpleQuestion question) {
		String answer = "";
		while (!question.isValidAnswer(answer)) {
			System.out.println(question.getQuestion());
			answer = System.console().readLine();
		}
		return TypedResult.ok(answer);
	}

	private TypedResult<Integer> getNumberResponse(int low, int high) {
		String input = System.console().readLine();

		try {
			int number = Integer.parseInt(input);

			if (number < low || number > high) {
				return TypedResult.fail("Invalid input: %d", number);
			}

			return TypedResult.ok(number);
		} catch (NumberFormatException nfe) {
			return TypedResult.fail(nfe.getMessage());
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
