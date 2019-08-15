/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.java.analyzer;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.expr.SimpleName;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.Location;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

public abstract class Denomination<T extends Denomination<T,I>, I extends Enum<I> & IInsertionPoint<T>> extends AccessModifiable<T,I> {

	private final String packageName;

	private final String denominationName;

	private final SimpleName rawNameNode;

	private final Location nameLocation;

	private final AnalyzedSourceFile sourceFile;

	public Denomination(@Nonnull AnalyzedSourceFile sourceFile,
						@Nonnull Location location, List<Modifier.Keyword> modifiers,
						@Nonnull String packageName,
						@Nonnull LocatedName<SimpleName> name
	) {
		super(location, modifiers);
		this.sourceFile = sourceFile;
		this.rawNameNode = name.getRaw();
		this.nameLocation = name.getLocation();
		this.packageName = packageName;
		this.denominationName = name.getName();
	}

	@Nonnull
	public AnalyzedSourceFile getSourceFile() {
		return sourceFile;
	}

	@Nonnull
	public SimpleName getRawNameNode() {
		return rawNameNode;
	}

	@Nonnull
	public Location getNameLocation() {
		return nameLocation;
	}

	@Nonnull
	public String getDenominationName() {
		return denominationName;
	}

	@Nonnull
	public String getPackageName() {
		return packageName;
	}
}
