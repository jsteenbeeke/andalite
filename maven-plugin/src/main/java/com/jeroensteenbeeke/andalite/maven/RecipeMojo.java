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
package com.jeroensteenbeeke.andalite.maven;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.hyperion.util.ActionResult;
import com.jeroensteenbeeke.andalite.forge.ForgeRecipe;

public abstract class RecipeMojo extends AbstractMojo {
	@Parameter
	protected String[] recipes;

	@Parameter
	protected Map<String, String> extraConfiguration;

	public RecipeMojo() {
		super();
	}

	protected List<ForgeRecipe> determineRecipes() throws MojoFailureException {
		List<ForgeRecipe> recipeList = Lists.newArrayList();

		for (String className : recipes) {
			try {
				Class<?> recipeClass = Class.forName(className);

				if (ForgeRecipe.class.isAssignableFrom(recipeClass)) {
					try {
						// Check for constructor with map parameter
						Constructor<?> constructor = recipeClass
								.getConstructor(Map.class);

						ForgeRecipe recipe = (ForgeRecipe) constructor
								.newInstance(extraConfiguration);
						
						ActionResult configured = recipe.checkCorrectlyConfigured();
						
						if (configured.isOk()) {
							recipeList.add(recipe);
						} else {
							getLog().warn(String.format("Recipe %s not correctly configured: %s", className, configured.getMessage()));
						}
					} catch (NoSuchMethodException e) {
						// And if it doesn't exist, go for a default no-argument
						// constructor
						recipeList.add((ForgeRecipe) recipeClass.newInstance());

					}
				} else {
					getLog().error(
							"Class "
									+ recipeClass.getName()
									+ " does not implement the ForgeRecipe interface");
				}
			} catch (ClassNotFoundException e) {
				getLog().error("Recipe not found: " + className);
				getLog().error(e);
			} catch (InstantiationException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				getLog().error("Could not instantiate recipe: " + className);
				getLog().error(e);
			}
		}

		if (recipeList.isEmpty()) {
			throw new MojoFailureException("Unable to instantiate any recipes!");
		}
		return recipeList;
	}

}
