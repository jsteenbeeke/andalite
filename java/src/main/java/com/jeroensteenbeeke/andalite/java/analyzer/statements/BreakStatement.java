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
package com.jeroensteenbeeke.andalite.java.analyzer.statements;

import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedExpression;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedStatement;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BreakStatement extends BaseStatement<BreakStatement> {
	private final String labelId;

	public BreakStatement(@NotNull Location location, @Nullable String labelId) {
		super(location);
		this.labelId = labelId;
	}

	@NotNull
	public Optional<String> getExpression() {
		return Optional.ofNullable(labelId);
	}

	@Override
	public String toJavaString() {
		if (labelId != null) {
			return String.format("break %s", labelId);
		}

		return "break";
	}

}
