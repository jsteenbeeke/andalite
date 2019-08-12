package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.SourceFileSelectQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;

import javax.annotation.Nonnull;
import java.util.List;

public class SourceFileSelectionTemplate extends QuestionTemplate<SourceFileSelectionTemplate, SourceFileSelectQuestion> {

	private final ImmutableList<AnalyzedSourceFile> choices;

	private final QuestionCreator questionCreator;

	SourceFileSelectionTemplate(@Nonnull String key, @Nonnull String question) {
		super(key, question);
		this.choices = ImmutableList.of();
		this.questionCreator = SourceFileSelectQuestion.SimpleSourceFileSelectQuestion::new;
	}

	private SourceFileSelectionTemplate(String key,
										String question,
										ImmutableList<FollowUp> followupQuestion,
										ImmutableList<AnalyzedSourceFile> choices,
										QuestionCreator questionCreator, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
		this.choices = choices;
		this.questionCreator = questionCreator;
	}

	public SourceFileSelectionTemplate withSources(@Nonnull AnalyzedSourceFile... choices) {
		return new SourceFileSelectionTemplate(getKey(), getQuestion(), getFollowupQuestion(),
											   ImmutableList.<AnalyzedSourceFile>builder()
													  .addAll(this.choices)
													  .addAll(ImmutableList.copyOf(choices))
													  .build(),
											   questionCreator,
											   getFormatter());
	}


	@Override
	protected SourceFileSelectionTemplate newInstance(@Nonnull String key, @Nonnull String question, @Nonnull ImmutableList<FollowUp> followupQuestions, @Nonnull CheckedBiFunction<Answers, String, String> formatter) {
		return new SourceFileSelectionTemplate(key, question, followupQuestions, choices, questionCreator, formatter);
	}

	public SourceFileSelectionTemplate withQuestionCreator(@Nonnull QuestionCreator questionCreator) {
		return new SourceFileSelectionTemplate(getKey(), getQuestion(), getFollowupQuestion(), choices, questionCreator, getFormatter());
	}

	@FunctionalInterface
	public interface QuestionCreator {
		SourceFileSelectQuestion createQuestion(@Nonnull String key, @Nonnull String question, @Nonnull List<AnalyzedSourceFile> choices);
	}

	@Override
	public SourceFileSelectQuestion toQuestion(@Nonnull Answers answers) throws ForgeException {
		return questionCreator.createQuestion(getKey(), getFormattedQuestion(answers), choices);
	}
}
