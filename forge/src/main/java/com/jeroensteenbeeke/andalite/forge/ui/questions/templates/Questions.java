package com.jeroensteenbeeke.andalite.forge.ui.questions.templates;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.questions.Answers;
import com.jeroensteenbeeke.andalite.forge.ui.questions.templates.fn.CheckedFunction;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.lux.TypedResult;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.List;
import java.util.function.Supplier;

public class Questions {
	public static WithKey<SimpleQuestionTemplate> simple(@NotNull String question) {
		return key -> new SimpleQuestionTemplate(key, question);
	}

	public static WithKey<YesNoQuestionTemplate> yesNo(@NotNull String question) {
		return key -> new YesNoQuestionTemplate(key, question);
	}

	public static WithChoices multipleChoice(@NotNull String question) {
		return choices -> key -> new MultipleChoiceQuestionTemplate(key, question)
			.withChoices(choices);
	}

	public static WithConditionalChoices conditionalMultipleChoice(@NotNull String question) {
		return choices -> key -> new ConditionalMultipleChoiceQuestionTemplate(key, question, choices);
	}

	public static WithFiles file(@NotNull String question) {
		return (first, rest) -> key -> new FileSelectionQuestionTemplate(key, question)
			.withChoices(first)
			.withChoices(rest);
	}

	public static WithSources sourceFile(@NotNull String question) {
		return (first, rest) -> key -> new SourceFileSelectionTemplate(key, question)
			.withSources(first)
			.withSources(rest);
	}


	@FunctionalInterface
	public interface WithKey<T extends QuestionTemplate<T, ?>> {
		@NotNull
		T withKey(@NotNull String key);
	}

	public interface WithChoices {
		WithKey<MultipleChoiceQuestionTemplate> withChoices(List<String> choices);

		default WithKey<MultipleChoiceQuestionTemplate> withChoices(@NotNull String first, @NotNull String... next) {
			return withChoices(ImmutableList.<String>builder().add(first).addAll(ImmutableList.copyOf(next)).build());
		}

	}

	public interface WithFiles {
		WithKey<FileSelectionQuestionTemplate> withFiles(@NotNull File first, @NotNull File... rest);

		default WithKey<FileSelectionQuestionTemplate> withFiles(@NotNull Supplier<List<File>> fileSupplier) {
			List<File> files = fileSupplier.get();

			if (files.isEmpty()) {
				throw new IllegalArgumentException("Empty list of files supplied");
			}

			return withFiles(files.get(0), files.subList(1, files.size()).toArray(File[]::new));
		}
	}

	public interface WithSources {
		WithKey<SourceFileSelectionTemplate> withSources(@NotNull AnalyzedSourceFile first, @NotNull AnalyzedSourceFile... rest);

		default WithKey<SourceFileSelectionTemplate> withSources(@NotNull Supplier<TypedResult<List<AnalyzedSourceFile>>> fileSupplier) {
			TypedResult<List<AnalyzedSourceFile>> result = fileSupplier.get();

			if (!result.isOk()) {
				throw new IllegalStateException(result.getMessage());
			}

			List<AnalyzedSourceFile> sourceFiles = result.getObject();
			if (sourceFiles.isEmpty()) {
				throw new IllegalArgumentException("Empty list of sourceFiles supplied");
			}

			return withSources(sourceFiles.get(0), sourceFiles
				.subList(1, sourceFiles.size())
				.toArray(AnalyzedSourceFile[]::new));
		}
	}

	public interface WithConditionalChoices {
		WithKey<ConditionalMultipleChoiceQuestionTemplate> withConditionalChoices(CheckedFunction<Answers, List<String>> choices);
	}
}
