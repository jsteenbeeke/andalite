package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedFunction;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.lux.TypedResult;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.function.Supplier;

public class Questions {
	public static WithKey<SimpleQuestionTemplate> simple(@Nonnull String question) {
		return key -> new SimpleQuestionTemplate(key, question);
	}

	public static WithKey<YesNoQuestionTemplate> yesNo(@Nonnull String question) {
		return key -> new YesNoQuestionTemplate(key, question);
	}

	public static WithChoices multipleChoice(@Nonnull String question) {
		return choices -> key -> new MultipleChoiceQuestionTemplate(key, question)
			.withChoices(choices);
	}

	public static WithConditionalChoices conditionalMultipleChoice(@Nonnull String question) {
		return choices -> key -> new ConditionalMultipleChoiceQuestionTemplate(key, question, choices);
	}

	public static WithFiles file(@Nonnull String question) {
		return (first, rest) -> key -> new FileSelectionQuestionTemplate(key, question)
			.withChoices(first)
			.withChoices(rest);
	}

	public static WithSources sourceFile(@Nonnull String question) {
		return (first, rest) -> key -> new SourceFileSelectionTemplate(key, question)
			.withSources(first)
			.withSources(rest);
	}


	@FunctionalInterface
	public interface WithKey<T extends QuestionTemplate<T, ?>> {
		@Nonnull
		T withKey(@Nonnull String key);
	}

	public interface WithChoices {
		WithKey<MultipleChoiceQuestionTemplate> withChoices(List<String> choices);

		default WithKey<MultipleChoiceQuestionTemplate> withChoices(@Nonnull String first, @Nonnull String... next) {
			return withChoices(ImmutableList.<String>builder().add(first).addAll(ImmutableList.copyOf(next)).build());
		}

	}

	public interface WithFiles {
		WithKey<FileSelectionQuestionTemplate> withFiles(@Nonnull File first, @Nonnull File... rest);

		default WithKey<FileSelectionQuestionTemplate> withFiles(@Nonnull Supplier<List<File>> fileSupplier) {
			List<File> files = fileSupplier.get();

			if (files.isEmpty()) {
				throw new IllegalArgumentException("Empty list of files supplied");
			}

			return withFiles(files.get(0), files.subList(1, files.size()).toArray(File[]::new));
		}
	}

	public interface WithSources {
		WithKey<SourceFileSelectionTemplate> withSources(@Nonnull AnalyzedSourceFile first, @Nonnull AnalyzedSourceFile... rest);

		default WithKey<SourceFileSelectionTemplate> withSources(@Nonnull Supplier<TypedResult<List<AnalyzedSourceFile>>> fileSupplier) {
			TypedResult<List<AnalyzedSourceFile>> result = fileSupplier.get();

			if (!result.isOk()) {
				throw new IllegalStateException(result.getMessage());
			}

			List<AnalyzedSourceFile> sourceFiles = result.getObject();
			if (sourceFiles.isEmpty()) {
				throw new IllegalArgumentException("Empty list of sourceFiles supplied");
			}

			return withSources(sourceFiles.get(0), sourceFiles
				.subList(0, sourceFiles.size())
				.toArray(new AnalyzedSourceFile[sourceFiles.size() - 1]));
		}
	}

	public interface WithConditionalChoices {
		WithKey<ConditionalMultipleChoiceQuestionTemplate> withConditionalChoices(CheckedFunction<Answers, List<String>> choices);
	}
}
