package com.jeroensteenbeeke.andalite.maven.ui;

import com.google.common.io.Files;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;
import com.jeroensteenbeeke.andalite.forge.ui.Question;
import com.jeroensteenbeeke.andalite.forge.ui.questions.*;
import com.jeroensteenbeeke.andalite.forge.ui.renderer.QuestionRenderer;
import com.jeroensteenbeeke.lux.TypedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class FileInputRenderer implements QuestionRenderer, FeedbackHandler {
	private static final Logger log = LoggerFactory
			.getLogger(MavenQuestionRenderer.class);

	private final List<String> input;

	public FileInputRenderer(File inputFile) throws IOException {
		input = Files.readLines(inputFile, Charset.forName("UTF-8"));

	}


	@Override
	public TypedResult<ForgeRecipe> renderRecipeSelection(
			@Nonnull
					List<ForgeRecipe> recipeList) {
		log.info("Select recipe:");
		for (int i = 0; i < recipeList.size(); i++) {
			log.info("\t{} - {}", (i+1), recipeList.get(i).getName());
		}
		try {
			int index = Integer.parseInt(input.remove(0));
			log.info("-> {}", index);
			return TypedResult.ok(recipeList.get(index-1));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			return TypedResult.fail(e.getMessage());
		}
	}

	@Override
	public TypedResult<Answers> renderQuestion(
			@Nonnull Answers answers,
			@Nonnull Question question) {

		String nextInput = input.remove(0);

		log.info(question.getQuestion());
		final String answerKey = question.getKey();
		if (question instanceof SimpleQuestion) {
			log.info("-> {}", nextInput);

			return TypedResult.ok(answers.plus(answerKey, nextInput));
		}
		if (question instanceof YesNoQuestion) {
			log.info("\tYes, No");

			boolean yesOrNo = Boolean.parseBoolean(nextInput);

			log.info("-> {}", yesOrNo ? "Yes" : "No");

			return TypedResult.ok(answers.plus(answerKey, yesOrNo));

		}
		if (question instanceof MultipleChoiceQuestion) {
			MultipleChoiceQuestion mcQuestion = (MultipleChoiceQuestion) question;

			log.info("\t{}", mcQuestion.getChoices().stream().collect(Collectors.joining(", ")));

			try {
				int choice = Integer.parseInt(nextInput);
				if (choice < 1 || choice > mcQuestion.getChoices().size()) {
					log.debug("Valid choices: 1 to {}", mcQuestion.getChoices().size());
					return TypedResult.fail("Invalid input: "
							+ choice);
				}

				String answer = mcQuestion.getChoices().get(choice - 1);

				log.info("-> {}", answer);

				return TypedResult.ok(answers.plus(answerKey, answer));
			} catch (NumberFormatException nfe) {
				return TypedResult
						.fail("Invalid input: " + nextInput);
			}

		}
		if (question instanceof FileSelectQuestion) {
			FileSelectQuestion fsQuestion = (FileSelectQuestion) question;

			log.info("\t{}", fsQuestion.getChoices().stream().map(File::getName).collect(Collectors.joining(", ")));

			try {
				int choice = Integer.parseInt(nextInput);
				if (choice < 1 || choice > fsQuestion.getChoices().size()) {
					log.debug("Valid choices: 1 to {}", fsQuestion.getChoices().size());
					return TypedResult.fail("Invalid input: "
							+ choice);
				}

				File answer = fsQuestion.getChoices().get(choice - 1);

				log.info("-> {}", answer.getName());

				return TypedResult.ok(answers.plus(answerKey, answer));
			} catch (NumberFormatException nfe) {
				return TypedResult.fail("Invalid input: " + nextInput);
			}
		}

		return TypedResult.fail("Unknown question type: %s", question
				.getClass().getName());
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
