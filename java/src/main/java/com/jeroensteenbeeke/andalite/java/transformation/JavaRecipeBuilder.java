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

package com.jeroensteenbeeke.andalite.java.transformation;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.andalite.core.ILocatable;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedSourceFile;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.CompilationUnitNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PackageClassNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PackageEnumNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PackageInterfaceNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PublicClassNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PublicEnumNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.PublicInterfaceNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.ICompilationUnitOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureImports;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsurePackageClass;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsurePackageEnum;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsurePackageInterface;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsurePublicClass;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsurePublicEnum;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsurePublicInterface;

public class JavaRecipeBuilder implements IStepCollector,
		IScopedOperationBuilder<AnalyzedSourceFile, ICompilationUnitOperation> {
	private final List<JavaRecipeStep<?>> steps;

	public JavaRecipeBuilder() {
		this.steps = Lists.newArrayList();
	}

	@Override
	public <T extends ILocatable> void addStep(IJavaNavigation<T> nav,
			IJavaOperation<T> oper) {
		this.steps.add(new JavaRecipeStep<T>(nav, oper));
	}

	@Nonnull
	public JavaRecipe build() {
		return new JavaRecipe(steps);
	}

	@Nonnull
	public ClassScopeOperationBuilder inPublicClass() {
		return new ClassScopeOperationBuilder(this,
				new PublicClassNavigation());
	}

	@Nonnull
	public ClassScopeOperationBuilder inPackageClass(@Nonnull String name) {
		return new ClassScopeOperationBuilder(this,
				new PackageClassNavigation(name));
	}

	public InterfaceScopeOperationBuilder inPublicInterface() {
		return new InterfaceScopeOperationBuilder(this,
				new PublicInterfaceNavigation());
	}

	public InterfaceScopeOperationBuilder inPackageInterface(
			@Nonnull String name) {
		return new InterfaceScopeOperationBuilder(this,
				new PackageInterfaceNavigation(name));
	}

	public EnumScopeOperationBuilder inPublicEnum() {
		return new EnumScopeOperationBuilder(this, new PublicEnumNavigation());
	}

	public EnumScopeOperationBuilder inPackageEnum(@Nonnull String name) {
		return new EnumScopeOperationBuilder(this,
				new PackageEnumNavigation(name));
	}

	public void ensureImport(@Nonnull String fqdn) {
		ensure(new EnsureImports(fqdn));
	}

	public void ensurePublicClass() {
		ensure(new EnsurePublicClass());
	}

	public void ensurePackageClass(@Nonnull String name) {
		ensure(new EnsurePackageClass(name));
	}

	public void ensurePublicInterface() {
		ensure(new EnsurePublicInterface());
	}

	public void ensurePackageInterface(@Nonnull String name) {
		ensure(new EnsurePackageInterface(name));
	}

	public void ensurePublicEnum() {
		ensure(new EnsurePublicEnum());
	}

	public void ensurePackageEnum(@Nonnull String name) {
		ensure(new EnsurePackageEnum(name));
	}

	@Override
	public void ensure(ICompilationUnitOperation operation) {
		addStep(CompilationUnitNavigation.getInstance(), operation);
	}

}
