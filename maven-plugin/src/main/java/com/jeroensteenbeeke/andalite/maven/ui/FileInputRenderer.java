package com.jeroensteenbeeke.andalite.maven.ui;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
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

public class FileInputRenderer implements QuestionRenderer, FeedbackHandler {
	private static final Logger log = LoggerFactory
			.getLogger(MavenQuestionRenderer.class);

	private final List<String> input;

	public FileInputRenderer(File inputFile) throws IOException {
		input = Files.readLines(inputFile, Charset.forName("UTF-8"));

	}

	@Override
	public <T> TypedActionResult<Action> renderQuestion(Question<T> question) {
		String nextInput = input.remove(0);

		try {
			log.info(question.getQuestion());
			if (question instanceof SimpleQuestion) {
				log.info("-> {}", nextInput);
				
				Action answer = ((SimpleQuestion) question).onAnswer(nextInput,
						this);

				return TypedActionResult.ok(answer);
			}
			if (question instanceof YesNoQuestion) {
				YesNoQuestion yesNoQuestion = (YesNoQuestion) question;
				log.info("\tYes, No");
				
				boolean yesOrNo = Boolean.parseBoolean(nextInput);
				
				log.info("-> {}", yesOrNo ? "Yes" : "No");
				
				Action answer = yesNoQuestion.onAnswer(
						yesOrNo, this);
				
				

				return TypedActionResult.ok(answer);
			}
			if (question instanceof MultipleChoiceQuestion) {
				MultipleChoiceQuestion mcQuestion = (MultipleChoiceQuestion) question;
				
				log.info("\t{}", mcQuestion.getChoices().stream().collect(Collectors.joining(", ")));
				
				try {
					int choice = Integer.parseInt(nextInput);
					if (choice < 1 || choice > mcQuestion.getChoices().size()) {
						log.debug("Valid choices: 1 to {}", mcQuestion.getChoices().size());
						return TypedActionResult.fail("Invalid input: "
								+ choice);
					}
					
					String answer = mcQuestion.getChoices().get(choice-1);
					
					log.info("-> {}", answer);
					
					Action action = mcQuestion.onAnswer(answer, this);
					return TypedActionResult.ok(action);
				} catch (NumberFormatException nfe) {
					return TypedActionResult
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
						return TypedActionResult.fail("Invalid input: "
								+ choice);
					}
					
					File answer = fsQuestion.getChoices().get(choice-1);
					
					log.info("-> {}", answer.getName());
					
					Action action = fsQuestion.onAnswer(answer, this);
					return TypedActionResult.ok(action);
				} catch (NumberFormatException nfe) {
					return TypedActionResult
							.fail("Invalid input: " + nextInput);
				}
			}
			if (question instanceof RecipeSelectionQuestion) {
				RecipeSelectionQuestion recipeQuestion = (RecipeSelectionQuestion) question;
				
				try {
					final int choice = Integer.parseInt(nextInput);
					final int maxChoice = 1 + recipeQuestion.getRecipes().size();
					
					log.info("\t{}, Done", recipeQuestion.getRecipes().stream().map(ForgeRecipe::getName).collect(Collectors.joining(", ")));
					
					if (choice < 1 || choice > maxChoice) {
						log.debug("Valid choices: 1 to {}", maxChoice);
						return TypedActionResult.fail("Invalid input: "
								+ choice);
					}
					
					if (choice == maxChoice) {
						log.info("-> Done");
						return TypedActionResult.ok(Completed.get());
					}
					
					ForgeRecipe recipe = recipeQuestion.getRecipes().get(choice-1);
					
					log.info("-> {}", recipe.getName());
					
					Action action = recipeQuestion.onAnswer(recipe, this);
					return TypedActionResult.ok(action);
				} catch (NumberFormatException nfe) {
					return TypedActionResult
							.fail("Invalid input: " + nextInput);
				}
			}
		} catch (ForgeException e) {
			return TypedActionResult.fail(e.getMessage());
		}

		return TypedActionResult.fail("Unknown question type: %s", question
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
