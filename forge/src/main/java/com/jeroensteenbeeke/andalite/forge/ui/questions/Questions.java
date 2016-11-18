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

		/**
		 * Return the answer with the given key
		 * 
		 * @param type
		 *            The expected type of the answer
		 * @param key
		 *            The key the answer is tied to
		 * @return The given answer, cast to the given type
		 * @throws IllegalStateException
		 *             If the answer is not of the given type
		 * @throws IllegalArgumentException
		 *             If no answer is registered with the given key
		 */
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

		/**
		 * Add an answer with the given key
		 * 
		 * @param key
		 *            The key with which to find the answer
		 * @param answer
		 *            The answer to add
		 */
		private void register(String key, Object answer) {
			if (answers.containsKey(key)) {
				throw new IllegalArgumentException("Duplicate key " + key);
			}
			answers.put(key, answer);

		}
	}

	/**
	 * DSL builder stage for adding a question to a given key
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static class QuestionStage {
		private final String key;

		private final List<QuestionInitiator> initiators;

		/**
		 * Create the stage for the given key
		 * 
		 * @param key
		 *            The key to tie the answer to
		 */
		private QuestionStage(String key) {
			this.key = key;
			this.initiators = Lists.newArrayList();
		}

		/**
		 * Create the stage for the given key, with the given initiators for
		 * previous questions
		 * 
		 * @param key
		 *            The key to tie the answer to
		 * @param initiators
		 *            A list of objects that will initiate earlier questions
		 */
		private QuestionStage(String key, List<QuestionInitiator> initiators) {
			this.key = key;
			this.initiators = initiators;
		}

		/**
		 * Set the question to ask for the given key
		 * 
		 * @param question
		 *            The key
		 * @return A builder object for specifying the type of question
		 */
		public TypeStage ask(String question) {
			return new TypeStage(initiators, key, question);
		}
	}

	/**
	 * DSL builder stage for specifying the question type
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static class TypeStage {

		private final List<QuestionInitiator> initiators;

		private final String key;

		private final String question;

		/**
		 * Create a new type stage for the given key and question, with optional
		 * list
		 * of previous questions
		 * 
		 * @param initiators
		 *            Initiators for previous questions
		 * @param key
		 *            The key this question's answer is tied to
		 * @param question
		 *            The question to ask
		 */
		private TypeStage(List<QuestionInitiator> initiators, String key,
				String question) {
			this.initiators = initiators;
			this.key = key;
			this.question = question;
		}

		/**
		 * Create a question with a simple (String) answer
		 * 
		 * @return The next builder stage
		 */
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

		/**
		 * Create a question with a String answer that must match the given
		 * pattern
		 *
		 * @param pattern
		 *            The pattern to which the question must adhere
		 * @return The next builder stage
		 * @see java.util.regex.Pattern
		 */
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

		/**
		 * Create a question that has either yes or no as an answer
		 * 
		 * @return The next builder stage
		 */
		public FinalizerStage withYesNoAnswer() {
			return new FinalizerStage(initiators, key, question, (q, a, cf) -> {
				return new YesNoQuestion(q) {

					@Override
					public Action onAnswer(Boolean answer,
							FeedbackHandler handler) throws ForgeException {

						return cf.apply(answer);
					}
				};
			});
		}

		/**
		 * Create a multiple choice question with a dynamic set of available
		 * choices
		 * 
		 * @param choices
		 *            A function that determines the list of available choices
		 *            based on earlier answers
		 * @return The next builder stage
		 */
		public FinalizerStage withMultipleChoiceAnswer(
				Function<Answers, List<String>> choices) {
			return new FinalizerStage(initiators, key, question, (q, a, cf) -> {
				return new MultipleChoiceQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {

						return cf.apply(answer);
					}

					@Override
					public List<String> getChoices() {
						return choices.apply(a);
					}
				};
			});
		}

		/**
		 * Create a multiple choice question with a predetermined set of
		 * available
		 * choices
		 * 
		 * @param choices
		 *            The available choices
		 * @return The next builder stage
		 */
		public FinalizerStage withMultipleChoiceAnswer(List<String> choices) {
			return new FinalizerStage(initiators, key, question, (q, a, cf) -> {
				return new MultipleChoiceQuestion(q) {

					@Override
					public Action onAnswer(String answer,
							FeedbackHandler handler) throws ForgeException {

						return cf.apply(answer);
					}

					@Override
					public List<String> getChoices() {
						return choices;
					}
				};
			});
		}

		/**
		 * Create a custom question type
		 * 
		 * @param nextQuestion
		 *            A factory for creating the next question. This is a
		 *            functional interface
		 *            so can be written as a Lambda-expression
		 * @return The next builder stage
		 */
		public FinalizerStage withCustomAnswer(QuestionFactory nextQuestion) {
			return new FinalizerStage(initiators, key, question, nextQuestion);
		}
	}

	public static class FinalizerStage {
		private final List<QuestionInitiator> initiators;

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

	/**
	 * Factory for the creation of questions
	 * 
	 * @author Jeroen Steenbeeke
	 */
	@FunctionalInterface
	public interface QuestionFactory {
		/**
		 * Create an action based on the given question and previous answers
		 * 
		 * @param question
		 *            The question to ask
		 * @param previousAnswers
		 *            All answers given until this point
		 * @param chainFinalizer
		 *            A callback mechanism for registering answers and invoking
		 *            the next question. Once
		 *            a question has been properly answered its answer should be
		 *            passed to this finalizer, and its result returned as this
		 *            method return value
		 * @return In case of success: the result of
		 *         {@code chainFinalizer.apply}, otherwise: any further action
		 *         that can be taken instead (usually graceful termination by
		 *         returning
		 *         {@code com.jeroensteenbeeke.andalite.forge.ui.actions.Failure}).
		 */
		Action initialize(String question, Answers previousAnswers,
				Function<Object, Action> chainFinalizer);
	}
}
