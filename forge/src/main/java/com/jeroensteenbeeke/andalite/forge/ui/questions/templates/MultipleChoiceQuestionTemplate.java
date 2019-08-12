package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.FileSelectQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.MultipleChoiceQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.SimpleQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class MultipleChoiceQuestionTemplate extends QuestionTemplate<MultipleChoiceQuestionTemplate, MultipleChoiceQuestion> {

	private final ImmutableList<String> choices;

	private final QuestionCreator questionCreator;

	MultipleChoiceQuestionTemplate(@Nonnull String key, @Nonnull String question) {
		super(key, question);
		this.choices = ImmutableList.of();
		this.questionCreator = MultipleChoiceQuestion.SimpleMultipleChoiceQuestion::new;
	}

	private MultipleChoiceQuestionTemplate(String key, String question, ImmutableList<FollowUp> followupQuestion, ImmutableList<String> choices, QuestionCreator questionCreator, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
		this.choices = choices;
		this.questionCreator = questionCreator;
	}

	public MultipleChoiceQuestionTemplate withChoices(@Nonnull String... choices) {
		return withChoices(List.of(choices));
	}

	public MultipleChoiceQuestionTemplate withChoices(@Nonnull List<String> choices) {
		return new MultipleChoiceQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(),
												  ImmutableList.<String>builder()
													  .addAll(this.choices)
													  .addAll(choices)
													  .build(),
												  questionCreator, getFormatter()
		);
	}

	@Override
	protected MultipleChoiceQuestionTemplate newInstance(@Nonnull String key, @Nonnull String question, @Nonnull ImmutableList<FollowUp> followupQuestions, @Nonnull CheckedBiFunction<Answers, String, String> formatter) {
		return new MultipleChoiceQuestionTemplate(key, question, followupQuestions, choices, questionCreator, formatter);
	}

	public MultipleChoiceQuestionTemplate withQuestionCreator(@Nonnull QuestionCreator questionCreator) {
		return new MultipleChoiceQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(), choices, questionCreator, getFormatter());
	}

	@FunctionalInterface
	public interface QuestionCreator {
		MultipleChoiceQuestion createQuestion(@Nonnull String key, @Nonnull String question, @Nonnull List<String> choices);
	}

	@Override
	public MultipleChoiceQuestion toQuestion(@Nonnull Answers answers) {
		return questionCreator.createQuestion(getKey(), getQuestion(), choices);
	}
}
