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
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jeroensteenbeeke.andalite.core.*;

public abstract class Annotatable<T extends Annotatable<T,I>, I extends Enum<I> & IInsertionPoint<T>> extends Locatable implements
		IAnnotatable, IAnnotationAddable<T>, IInsertionPointProvider<T,I> {
	private final Map<String, AnalyzedAnnotation> annotations;

	protected Annotatable(Location location) {
		super(location);
		this.annotations = Maps.newHashMap();
	}

	@NotNull
	public final List<AnalyzedAnnotation> getAnnotations() {
		return ImmutableList.copyOf(annotations.values());
	}

	public final boolean hasAnnotation(@NotNull String type) {
		return annotations.containsKey(type);
	}

	@CheckForNull
	public final AnalyzedAnnotation getAnnotation(@NotNull String type) {
		return annotations.get(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addAnnotation(@NotNull AnalyzedAnnotation annotation) {
		this.annotations.put(annotation.getType(), annotation);
		return (T) this;
	}

	void addAnnotations(@NotNull Iterable<AnalyzedAnnotation> annots) {
		for (AnalyzedAnnotation analyzedAnnotation : annots) {
			addAnnotation(analyzedAnnotation);
		}
	}

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

	public abstract void onOutput(IOutputCallback callback);

	public abstract I getAnnotationInsertPoint();
}
