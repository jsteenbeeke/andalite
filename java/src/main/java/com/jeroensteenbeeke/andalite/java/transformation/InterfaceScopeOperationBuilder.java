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

import org.jetbrains.annotations.NotNull;

import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedInterface;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.InnerClassNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IInterfaceOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureSuperInterface;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.InterfaceMethodType;

public class InterfaceScopeOperationBuilder extends
		AbstractOperationBuilder<AnalyzedInterface, IInterfaceOperation> {

	InterfaceScopeOperationBuilder(IStepCollector collector,
			IJavaNavigation<AnalyzedInterface> navigation) {
		super(collector, navigation);
	}

	public ClassScopeOperationBuilder forInnerClass(String name) {
		return new ClassScopeOperationBuilder(getCollector(),
											  new InnerClassNavigation<>(getNavigation(),
																		 name));
	}

	public AnnotatableOperationBuilder<AnalyzedInterface> forAnnotation(
			String type) {
		return new AnnotatableOperationBuilder<>(
			getCollector(), getNavigation(), type);
	}

	public InterfaceMethodLocator forMethod() {
		return new InterfaceMethodLocator(getCollector(), getNavigation());
	}

	public EnsureInterfaceMethodBuilder ensureMethod() {
		return new EnsureInterfaceMethodBuilder(InterfaceMethodType.ABSTRACT, this::ensure);
	}

	public EnsureInterfaceMethodBuilder ensureDefaultMethod() {
		return new EnsureInterfaceMethodBuilder(InterfaceMethodType.DEFAULT, this::ensure);
	}

	public EnsureInterfaceMethodBuilder ensurePrivateMethod() {
		return new EnsureInterfaceMethodBuilder(InterfaceMethodType.PRIVATE, this::ensure);
	}

	public EnsureInterfaceMethodBuilder ensureStaticMethod() {
		return new EnsureInterfaceMethodBuilder(InterfaceMethodType.PUBLIC_STATIC, this::ensure);
	}

	public EnsureInterfaceMethodBuilder ensurePrivateStaticMethod() {
		return new EnsureInterfaceMethodBuilder(InterfaceMethodType.PRIVATE_STATIC, this::ensure);
	}


	public void ensureSuperInterface(@NotNull String iface) {
		ensure(new EnsureSuperInterface(iface));
	}
}
