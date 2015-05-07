package com.jeroensteenbeeke.andalite.xml;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.core.ActionResult;

public class XMLRecipe {
	private static final Logger logger = LoggerFactory
			.getLogger(XMLRecipe.class);

	private final List<XMLRecipeStep<?>> steps;

	public XMLRecipe(List<XMLRecipeStep<?>> steps) {
		super();
		this.steps = steps;
	}

	public ActionResult applyTo(File file) {
		logger.info("Applying transformation ({} steps) to {}", steps.size(),
				file.getName());

		for (XMLRecipeStep<?> step : steps) {

		}

		logger.debug("All steps executed, checking if resulting file can be parsed");

		return ActionResult.error("Not yet implemented");
	}

}
