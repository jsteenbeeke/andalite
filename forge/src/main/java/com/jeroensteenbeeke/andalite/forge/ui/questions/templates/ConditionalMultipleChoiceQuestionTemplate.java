package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.MultipleChoiceQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedFunction;

import javax.annotation.Nonnull;
import java.util.List;

public class ConditionalMultipleChoiceQuestionTemplate extends QuestionTemplate<ConditionalMultipleChoiceQuestionTemplate, MultipleChoiceQuestion> {

	private final CheckedFunction<Answers, List<String>> choices;

	private final QuestionCreator questionCreator;

	ConditionalMultipleChoiceQuestionTemplate(@Nonnull String key, @Nonnull String question, @Nonnull CheckedFunction<Answers, List<String>> choices) {
		super(key, question);
		this.choices = choices;
		this.questionCreator = MultipleChoiceQuestion.SimpleMultipleChoiceQuestion::new;
	}

	private ConditionalMultipleChoiceQuestionTemplate(String key, String question, ImmutableList<FollowUp> followupQuestion, CheckedFunction<Answers, List<String>> choices, QuestionCreator questionCreator, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
		this.choices = choices;
		this.questionCreator = questionCreator;
	}

	@Override
	protected ConditionalMultipleChoiceQuestionTemplate newInstance(@Nonnull String key, @Nonnull String question, @Nonnull ImmutableList<FollowUp> followupQuestions, @Nonnull CheckedBiFunction<Answers, String, String> formatter) {
		return new ConditionalMultipleChoiceQuestionTemplate(key, question, followupQuestions, choices, questionCreator, formatter);
	}


	public ConditionalMultipleChoiceQuestionTemplate withQuestionCreator(@Nonnull QuestionCreator questionCreator) {
		return new ConditionalMultipleChoiceQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(), choices, questionCreator, getFormatter());
	}

	@FunctionalInterface
	public interface QuestionCreator {
		MultipleChoiceQuestion createQuestion(@Nonnull String key, @Nonnull String question, @Nonnull List<String> choices);
	}

	@Override
	public MultipleChoiceQuestion toQuestion(@Nonnull Answers answers) throws ForgeException {
		return questionCreator.createQuestion(getKey(), getFormattedQuestion(answers), choices.apply(answers));
	}
}
