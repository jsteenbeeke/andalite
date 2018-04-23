package com.jeroensteenbeeke.andalite.forge.ui.questions;

import com.google.common.collect.ImmutableMap;

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

	@SuppressWarnings("unchecked")
	public <T> T getAnswer(Class<T> type, String key) {
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
