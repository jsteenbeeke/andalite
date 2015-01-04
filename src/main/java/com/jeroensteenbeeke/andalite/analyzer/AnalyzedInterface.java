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
package com.jeroensteenbeeke.andalite.analyzer;

import com.jeroensteenbeeke.andalite.Location;

public class AnalyzedInterface extends ContainingDenomination {

	public AnalyzedInterface(Location location, int modifiers,
			String packageName, String denominationName) {
		super(location, modifiers, packageName, denominationName);
	}

	public String getInterfaceName() {
		return getDenominationName();
	}

	@Override
	public boolean isAutoAbstractMethods() {
		return true;
	}

	@Override
	public void onModifierOutputted(IOutputCallback callback) {
		callback.write("interface ");
		callback.write(getInterfaceName());
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		for (AnalyzedField analyzedField : getFields()) {
			analyzedField.output(callback);
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

}
