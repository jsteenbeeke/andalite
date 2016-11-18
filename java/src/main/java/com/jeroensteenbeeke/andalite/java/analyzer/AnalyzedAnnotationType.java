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

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

/**
 * Representation of an annotation type definition
 * 
 * @author Jeroen Steenbeeke
 */
public class AnalyzedAnnotationType extends ContainingDenomination {

	AnalyzedAnnotationType(@Nonnull Location location, int modifiers,
			@Nonnull String packageName,
			@Nonnull TerminalNode denominationName) {
		super(location, modifiers, packageName, denominationName);
	}

	/**
	 * Get the name of the annotation
	 * 
	 * @return The name of the annotation
	 */
	@Nonnull
	public String getAnnotationName() {
		return getDenominationName();
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("@interface ");
		callback.write(getAnnotationName());
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();

		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
		callback.newline();

	}

}
