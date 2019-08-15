package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.FileSelectQuestion;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedBiFunction;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class FileSelectionQuestionTemplate extends QuestionTemplate<FileSelectionQuestionTemplate, FileSelectQuestion> {

	private final ImmutableList<File> choices;

	private final QuestionCreator questionCreator;

	FileSelectionQuestionTemplate(@Nonnull String key, @Nonnull String question) {
		super(key, question);
		this.choices = ImmutableList.of();
		this.questionCreator = FileSelectQuestion.SimpleFileSelectQuestion::new;
	}

	private FileSelectionQuestionTemplate(String key,
										  String question,
										  ImmutableList<FollowUp> followupQuestion,
										  ImmutableList<File> choices,
										  QuestionCreator questionCreator, CheckedBiFunction<Answers, String, String> formatter) {
		super(key, question, followupQuestion, formatter);
		this.choices = choices;
		this.questionCreator = questionCreator;
	}

	public FileSelectionQuestionTemplate withChoices(@Nonnull File... choices) {
		return new FileSelectionQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(),
												 ImmutableList.<File>builder()
													  .addAll(this.choices)
													  .addAll(ImmutableList.copyOf(choices))
													  .build(),
												 questionCreator,
												 getFormatter());
	}

	@Override
	protected FileSelectionQuestionTemplate newInstance(@Nonnull String key, @Nonnull String question, @Nonnull ImmutableList<FollowUp> followupQuestions, @Nonnull CheckedBiFunction<Answers, String, String> formatter) {
		return new FileSelectionQuestionTemplate(key, question, followupQuestions, choices, questionCreator, formatter);
	}

	public FileSelectionQuestionTemplate withQuestionCreator(@Nonnull QuestionCreator questionCreator) {
		return new FileSelectionQuestionTemplate(getKey(), getQuestion(), getFollowupQuestion(), choices, questionCreator, getFormatter());
	}

	@FunctionalInterface
	public interface QuestionCreator {
		FileSelectQuestion createQuestion(@Nonnull String key, @Nonnull String question, @Nonnull List<File> choices);
	}

	@Override
	public FileSelectQuestion toQuestion(@Nonnull Answers answers) throws ForgeException {
		return questionCreator.createQuestion(getKey(), getFormattedQuestion(answers), choices);
	}
}
