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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

public class AnalyzedEnum extends ConstructableDenomination {
	private final List<AnalyzedEnumConstant> constants;

	private Location separatorLocation;

	public AnalyzedEnum(Location location, int modifiers, String packageName,
			TerminalNode denominationName) {
		super(location, modifiers, packageName, denominationName);
		this.constants = Lists.newArrayList();
	}

	@Nonnull
	public String getEnumName() {
		return getDenominationName();
	}

	public void setSeparatorLocation(@Nonnull Location separatorLocation) {
		this.separatorLocation = separatorLocation;
	}

	@CheckForNull
	public Location getSeparatorLocation() {
		return separatorLocation;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("enum ");
		callback.write(getEnumName());
		outputInterfaces(callback);
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		for (AnalyzedField analyzedField : getFields()) {
			analyzedField.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedConstructor constructor : getConstructors()) {
			constructor.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedMethod analyzedMethod : getMethods()) {
			analyzedMethod.output(callback);
			callback.newline();
			callback.newline();
		}

		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
		callback.newline();

	}

	void addConstant(@Nonnull AnalyzedEnumConstant constant) {
		constants.add(constant);
	}

	@Nonnull
	public List<AnalyzedEnumConstant> getConstants() {
		return ImmutableList.copyOf(constants);
	}

}
