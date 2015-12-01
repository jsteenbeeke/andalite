package com.jeroensteenbeeke.andalite.java.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class RecordingBailErrorStrategy extends BailErrorStrategy {
	private static final Logger log = LoggerFactory
			.getLogger(RecordingBailErrorStrategy.class);

	private final File inputFile;

	private final List<String> exceptionMessages = Lists.newLinkedList();

	public RecordingBailErrorStrategy(File inputFile) {
		super();
		this.inputFile = inputFile;
	}

	@Override
	public void reportError(Parser recognizer, RecognitionException e) {
		if (e != null) {
			Throwable cause = e.getCause();
			String exceptionMessage = e.getMessage();
			while (exceptionMessage == null && cause != null) {
				exceptionMessage = cause.getMessage();

				cause = cause.getCause();
			}

			if (exceptionMessage == null && e instanceof NoViableAltException) {
				NoViableAltException nvae = (NoViableAltException) e;

				Vocabulary vocabulary = recognizer.getVocabulary();
				String expected = nvae.getExpectedTokens().toList().stream()
						.map(i -> vocabulary.getDisplayName(i))
						.collect(Collectors.joining(", "));
				Token offendingToken = nvae.getOffendingToken();
				exceptionMessage = String
						.format("Encountered %s on line %d, position %d, expected one of %s",
								vocabulary.getDisplayName(offendingToken
										.getType()), offendingToken.getLine(),
								offendingToken.getCharPositionInLine(),
								expected);

				log.warn("--- Offending lines:");
				try (BufferedReader br = new BufferedReader(new FileReader(
						inputFile))) {
					String in;
					int line = 1;
					final int min = offendingToken.getLine() - 2;
					final int max = offendingToken.getLine() + 2;

					while ((in = br.readLine()) != null && line <= max) {
						if (line >= min) {
							log.info("\t{}", in);
						}

						line++;
					}

				} catch (IOException e1) {
					log.error(e.getMessage(), e);
				}
				log.warn("--- End offending lines");
			}

			if (exceptionMessage != null) {
				exceptionMessages.add(exceptionMessage);
			}
		}

		super.reportError(recognizer, e);
	}

	public String getExceptionMessage() {
		return exceptionMessages.stream().collect(Collectors.joining(", "));
	}
}
