/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.forge.ui.questions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.forge.ForgeException;
import com.jeroensteenbeeke.andalite.forge.ui.Action;
import com.jeroensteenbeeke.andalite.forge.ui.FeedbackHandler;

/**
 * Utility class for quickly constructing a chain of questions that trigger
 * recipes
 * 
 * @author Jeroen Steenbeeke
 */
public class Questions {
	private Questions() {

	}

	/**
	 * Start creating a new question, whose answer will be tied to the given key
	 * 
	 * @param key
	 *            The key to tie the answer to
	 * @return A builder for creating a question
	 */
	@Nonnull
	public static QuestionStage forKey(@Nonnull String key) {
		return new QuestionStage(key);
	}

	/**
	 * Answer storage class. Essentially a wrapper around java.util.Map that
	 * allows
	 * for extra type-checking.
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static class Answers {
		private final Map<String, Object> answers;

		private Answers() {
			this.answers = Maps.newLinkedHashMap();
		}

		@SuppressWarnings("unchecked")
		public <T> T getAnswer(Class<T> type, String key) {
			if (answers.containsKey(key)) {
				Object object = answers.get(key);

				if (!type.isAssignableFrom(object.getClass())) {
					throw new IllegalStateException("Answer with key " + key
							+ " not of type " + type.getName() + " but of type "
							+ object.getClass().getName());
				}

				return (T) object;
			}

			throw new IllegalArgumentException(
					"No answer registered with key " + key);
		}

		private void register(String key, Object answer) {
			if (answers.containsKey(key)) {
				throw new IllegalArgumentException("Duplicate key " + key);
			}
			answers.put(key, answer);

		}
	}

	public static class QuestionStage {
		private final String key;

		private final List<QuestionInitiator> initiators;

		private QuestionStage(String key) {
			this.key = key;
			this.initiators = Lists.newArrayList();
		}

		private QuestionStage(String key, List<QuestionInitiator> initiators) {
			this.key = key;
			this.initiators = initiators;
		}

		public TypeStage ask(String question) {
			return new TypeStage(initiators, key, question);
		}
	}

	public static class TypeStage {

		private final List<QuestionInitiator> initiators;

		private final String key;

		private final String question;

		private TypeStage(List<QuestionInitiator> initiators, String key,
				String question) {
			this.initiators = initiators;
			this.key = key;
			this.question = question;
		}

		public FinalizerStage withSimpleAnswer() {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new SimpleQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {

						return s.apply(answer);
					}
				};
			});
		}

		public FinalizerStage withSimpleAnswerMatching(String pattern) {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new SimpleQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {
						if (!answer.matches(pattern)) {
							handler.error("Invalid input");
							return this;
						}

						return s.apply(answer);
					}
				};
			});
		}

		public FinalizerStage withYesNoAnswer() {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new YesNoQuestion(q) {

					@Override
					public Action onAnswer(Boolean answer,
							FeedbackHandler handler) throws ForgeException {

						return s.apply(answer);
					}
				};
			});
		}

		public FinalizerStage withMultipleChoiceAnswer(
				Function<Answers, List<String>> choices) {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new MultipleChoiceQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {

						return s.apply(answer);
					}

					@Override
					public List<String> getChoices() {
						return choices.apply(a);
					}
				};
			});
		}

		public FinalizerStage withMultipleChoiceAnswer(List<String> choices) {
			return new FinalizerStage(initiators, key, question, (q, a, s) -> {
				return new MultipleChoiceQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {

						return s.apply(answer);
					}

					@Override
					public List<String> getChoices() {
						return choices;
					}
				};
			});
		}

		public FinalizerStage withCustomAnswer(QuestionFactory nextQuestion) {
			return new FinalizerStage(initiators, key, question, nextQuestion);
		}
	}

	public static class FinalizerStage {
		private final List<QuestionInitiator> initiators;

		private FinalizerStage(String key, String question,
				QuestionFactory nextQuestion) {
			initiators = Lists.newArrayList();
			initiators.add(new QuestionInitiator(key, question, nextQuestion));
		}

		private FinalizerStage(List<QuestionInitiator> initiators, String key,
				String question, QuestionFactory nextQuestion) {
			this.initiators = initiators;
			initiators.add(new QuestionInitiator(key, question, nextQuestion));
		}

		public QuestionStage andForKey(String key) {
			return new QuestionStage(key, initiators);
		}

		public Action andThen(Function<Answers, Action> answersToAction) {
			QuestionInitiator initiator = initiators.remove(0);

			return initiator.initiate(initiators, answersToAction);
		}
	}

	private static class QuestionInitiator {
		private final String key;

		private final String question;

		private final QuestionFactory questionMaker;

		private QuestionInitiator(String key, String question,
				QuestionFactory questionMaker) {
			this.key = key;
			this.question = question;
			this.questionMaker = questionMaker;
		}

		public Action initiate(List<QuestionInitiator> next,
				Function<Answers, Action> answersToAction) {
			return initiate(new Answers(), next, answersToAction);
		}

		private Action initiate(Answers answers, List<QuestionInitiator> next,
				Function<Answers, Action> answersToAction) {
			return questionMaker.initialize(question, answers, s -> {
				answers.register(key, s);

				if (next.isEmpty()) {
					return answersToAction.apply(answers);
				}
				QuestionInitiator initiator = next.remove(0);

				return initiator.initiate(answers, next, answersToAction);

			});
		}

	}

	@FunctionalInterface
	public interface QuestionFactory {
		Action initialize(String question, Answers previousAnswers,
				Function<Object, Action> chainFinalizer);
	}
}
