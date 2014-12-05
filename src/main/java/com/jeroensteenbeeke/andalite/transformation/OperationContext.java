package com.jeroensteenbeeke.andalite.transformation;

import java.io.File;

import com.jeroensteenbeeke.andalite.ActionResult;

public interface OperationContext {

	ActionResult inFile(File file);
}
