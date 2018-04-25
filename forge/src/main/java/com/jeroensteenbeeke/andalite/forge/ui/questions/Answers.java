package com.jeroensteenbeeke.andalite.forge.ui.questions;

import com.google.common.collect.ImmutableMap;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.analyzer.ClassAnalyzer;
import com.jeroensteenbeeke.hyperion.util.TypedResult;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Map;

public class Answers {
	private final Map<String,Object> answers;

	private Answers(Map<String, Object> answers) {
		this.answers = answers;
	}

	public static Answers zero() {
		return new Answers(ImmutableMap.of());
	}

	public Answers plus(String key, Object value) {
		ImmutableMap.Builder<String,Object> builder = ImmutableMap.builder();
		builder.putAll(answers);
		return new Answers(builder.put(key,value).build());
	}

	public boolean hasAnswer(@Nonnull String key) {
		return answers.containsKey(key);
	}

	public String getString(@Nonnull String key) {
		return getAnswer(String.class, key);
	}

	public File getFile(@Nonnull String key) {
		return getAnswer(File.class, key);
	}

	public boolean getBoolean(@Nonnull String key) {
		return getAnswer(Boolean.class, key);
	}

	public TypedResult<AnalyzedSourceFile> getSource(@Nonnull String key) {
		return new ClassAnalyzer(getFile(key)).analyze();
	}

	private boolean hasAnswer(@Nonnull Class<?> type, @Nonnull String key) {
		return answers.containsKey(key) && type.isAssignableFrom(answers.get(key).getClass());
	}

	@SuppressWarnings("unchecked")
	private <T> T getAnswer(@Nonnull Class<T> type, @Nonnull String key) {
		if (answers.containsKey(key)) {
			Object object = answers.get(key);

			if (!type.isAssignableFrom(object.getClass())) {
				throw new IllegalStateException("Answer with key " + key
						+ " not of type " + type.getName()
						+ " but of type " + object.getClass().getName());
			}

			return (T) object;
		}

		System.err.println("Keys known:");
		answers.keySet().forEach(k -> System.err.printf("\t%s", k));

		throw new IllegalArgumentException("No answer registered with key "
				+ key);
	}
}
