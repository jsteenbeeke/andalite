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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.Location;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ConstructableDenomination<T extends ContainingDenomination<T,I>, I extends Enum<I> & IInsertionPoint<T>> extends ContainingDenomination<T,I> {

	private final List<AnalyzedConstructor> constructors;

	protected ConstructableDenomination(@Nonnull AnalyzedSourceFile sourceFile,
										@Nonnull Location location,
										@Nonnull List<Modifier.Keyword> modifiers,
										@Nonnull String packageName,
										@Nonnull LocatedName<SimpleName> name) {
		super(sourceFile, location, modifiers, packageName, name);
		this.constructors = Lists.newArrayList();
	}

	void addConstructor(@Nonnull AnalyzedConstructor constructor) {
		this.constructors.add(constructor);
	}

	@Nonnull
	public List<AnalyzedConstructor> getConstructors() {
		return ImmutableList.copyOf(constructors);
	}

}
