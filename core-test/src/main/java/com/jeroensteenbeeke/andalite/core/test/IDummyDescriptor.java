package com.jeroensteenbeeke.andalite.core.test;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@FunctionalInterface
public interface IDummyDescriptor {
	@NotNull
	File getDummy(@NotNull ITempFileRegister tempFiles) throws IOException;
}
