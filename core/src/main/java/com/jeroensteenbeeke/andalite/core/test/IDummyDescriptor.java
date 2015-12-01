package com.jeroensteenbeeke.andalite.core.test;

import java.io.File;
import java.io.IOException;

@FunctionalInterface
public interface IDummyDescriptor {
	File getDummy(ITempFileRegister tempFiles) throws IOException;
}
