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

package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Locatable;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Java language element that can have annotations
 * 
 * @author Jeroen Steenbeeke
 *
 */
public abstract class Annotatable extends Locatable implements IAnnotatable {
	private final Map<String, AnalyzedAnnotation> annotations;

	/**
	 * Create a new element that can have annotations
	 * 
	 * @param location
	 *            The location of the element
	 */
	protected Annotatable(Location location) {
		super(location);
		this.annotations = Maps.newHashMap();
	}

	/**
	 * Get the annotations on this element
	 * 
	 * @return An immutable list of the annotations on this element
	 */
	@Nonnull
	public final List<AnalyzedAnnotation> getAnnotations() {
		return ImmutableList.copyOf(annotations.values());
	}

	@Override
	public final boolean hasAnnotation(@Nonnull String type) {
		return annotations.containsKey(type);
	}

	@CheckForNull
	@Override
	public final AnalyzedAnnotation getAnnotation(@Nonnull String type) {
		return annotations.get(type);
	}

	/**
	 * Internal method for adding an annotation to this element
	 * 
	 * @param annotation
	 *            The annotation to add
	 */
	void addAnnotation(@Nonnull AnalyzedAnnotation annotation) {
		this.annotations.put(annotation.getType(), annotation);
	}

	/**
	 * Internal method for adding multiple annotations to this element
	 * 
	 * @param annots
	 *            An iterable containing annotations. May not contain null
	 * @throws IllegalArgumentException
	 *             If you did not read the above warning and you didn't filter
	 *             out null-values
	 */
	void addAnnotations(@Nonnull Iterable<AnalyzedAnnotation> annots) {
		for (AnalyzedAnnotation analyzedAnnotation : annots) {
			if (analyzedAnnotation == null) {
				throw new IllegalArgumentException(
						"Iterable contains null values");
			}

			addAnnotation(analyzedAnnotation);
		}
	}

	/**
	 * Determines whether or not annotations should be followed by a newline
	 * 
	 * @return {@code true} if a newline should follow the annotation,
	 *         {@code false} otherwise
	 */
	public boolean isPrintNewlineAfterAnnotation() {
		return true;
	}

	@Override
	public void output(IOutputCallback callback) {
		for (AnalyzedAnnotation analyzedAnnotation : getAnnotations()) {
			analyzedAnnotation.output(callback);
			if (isPrintNewlineAfterAnnotation()) {
				callback.newline();
			}
		}

		onOutput(callback);
	}

	/**
	 * Called after the annotations have been outputted
	 * 
	 * @param callback
	 *            The callback to write output to
	 */
	public abstract void onOutput(IOutputCallback callback);
}
