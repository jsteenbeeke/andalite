package com.jeroensteenbeeke.andalite.transformation;

import java.util.Random;

public class Randomizer {
	private static final Random random = new Random();

	private static final String AVAILABLE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private Randomizer() {

	}

	public static String random(int length) {
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < length; i++) {
			password.append(randomCharacter());
		}

		return password.toString();
	}

	private static char randomCharacter() {
		return AVAILABLE_CHARS.charAt(random.nextInt(AVAILABLE_CHARS.length()));
	}

	public static int randomLength(int min, int max) {
		return min + random.nextInt(max - min);
	}

}
