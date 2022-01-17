package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.MultipleChoiceQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedFunction;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ConditionalMultipleChoiceQuestionTemplate extends QuestionTemplate<ConditionalMultipleChoiceQuestionTemplate, MultipleChoiceQuestion> {

	private final CheckedFunction<Answers, List<String>> choices;

	private final QuestionCreator questionCreator;

	ConditionalMultipleChoiceQuestionTemplate(@NotNull String key, @NotNull String question, @NotNull CheckedFunction<Answers, List<String>> choices) {
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
	protected ConditionalMultipleChoiceQuestionTemplate newInstance(@NotNull String key, @NotNull String question, @NotNull ImmutableList<FollowUp> followupQuestions, @NotNull CheckedBiFunction<Answers, String, String> formatter) {
		return new ConditionalMultipleChoiceQuestionTemplate(key, question, followupQuestions, choices, questionCreator, formatter);
	}


	public ConditionalMultipleChoiceQuestionTemplate withQuestionCreator(@NotNull QuestionCreator questionCreator) {
		return new ConditionalMultipleChoiceQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(), choices, questionCreator, getFormatter());
	}

	@FunctionalInterface
	public interface QuestionCreator {
		MultipleChoiceQuestion createQuestion(@NotNull String key, @NotNull String question, @NotNull List<String> choices);
	}

	@Override
	public MultipleChoiceQuestion toQuestion(@NotNull Answers answers) throws ForgeException {
		return questionCreator.createQuestion(getKey(), getFormattedQuestion(answers), choices.apply(answers));
	}
}
