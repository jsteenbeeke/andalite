package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.FileSelectQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.MultipleChoiceQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.SimpleQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.List;

public class MultipleChoiceQuestionTemplate extends QuestionTemplate<MultipleChoiceQuestionTemplate, MultipleChoiceQuestion> {

	private final ImmutableList<String> choices;

	private final QuestionCreator questionCreator;

	MultipleChoiceQuestionTemplate(@NotNull String key, @NotNull String question) {
		super(key, question);
		this.choices = ImmutableList.of();
		this.questionCreator = MultipleChoiceQuestion.SimpleMultipleChoiceQuestion::new;
	}

	private MultipleChoiceQuestionTemplate(String key, String question, ImmutableList<FollowUp> followupQuestion, ImmutableList<String> choices, QuestionCreator questionCreator, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
		this.choices = choices;
		this.questionCreator = questionCreator;
	}

	public MultipleChoiceQuestionTemplate withChoices(@NotNull String... choices) {
		return withChoices(List.of(choices));
	}

	public MultipleChoiceQuestionTemplate withChoices(@NotNull List<String> choices) {
		return new MultipleChoiceQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(),
												  ImmutableList.<String>builder()
													  .addAll(this.choices)
													  .addAll(choices)
													  .build(),
												  questionCreator, getFormatter()
		);
	}

	@Override
	protected MultipleChoiceQuestionTemplate newInstance(@NotNull String key, @NotNull String question, @NotNull ImmutableList<FollowUp> followupQuestions, @NotNull CheckedBiFunction<Answers, String, String> formatter) {
		return new MultipleChoiceQuestionTemplate(key, question, followupQuestions, choices, questionCreator, formatter);
	}

	public MultipleChoiceQuestionTemplate withQuestionCreator(@NotNull QuestionCreator questionCreator) {
		return new MultipleChoiceQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(), choices, questionCreator, getFormatter());
	}

	@FunctionalInterface
	public interface QuestionCreator {
		MultipleChoiceQuestion createQuestion(@NotNull String key, @NotNull String question, @NotNull List<String> choices);
	}

	@Override
	public MultipleChoiceQuestion toQuestion(@NotNull Answers answers) {
		return questionCreator.createQuestion(getKey(), getQuestion(), choices);
	}
}
