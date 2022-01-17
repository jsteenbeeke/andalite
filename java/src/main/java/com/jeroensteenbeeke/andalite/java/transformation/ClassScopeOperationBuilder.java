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

import com.jeroensteenbeeke.andalite.java.analyzer.AccessModifier;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.IJavaNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.navigation.InnerClassNavigation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.*;

public class ClassScopeOperationBuilder
		extends AbstractOperationBuilder<AnalyzedClass, IClassOperation> {

	ClassScopeOperationBuilder(IStepCollector collector,
			IJavaNavigation<AnalyzedClass> navigation) {
		super(collector, navigation);
	}

	@NotNull
	public FieldOperationBuilder forField(String name) {
		return new FieldOperationBuilder(getCollector(), getNavigation(), name);
	}

	@NotNull
	public ClassScopeOperationBuilder forInnerClass(String name) {
		return new ClassScopeOperationBuilder(getCollector(),
			new InnerClassNavigation<>(getNavigation(), name));
	}

	@NotNull
	public AnnotatableOperationBuilder<AnalyzedClass> forAnnotation(
			String type) {
		return new AnnotatableOperationBuilder<>(getCollector(), getNavigation(), type);
	}

	@NotNull
	public ClassMethodLocator forMethod() {
		return new ClassMethodLocator(getCollector(), getNavigation());
	}

	@NotNull
	public ClassConstructorLocator forConstructor() {
		return new ClassConstructorLocator(getCollector(), getNavigation());
	}

	public void ensureAnnotation(@NotNull String annotation) {
		ensure(new EnsureClassAnnotation(annotation));
	}

	@NotNull
	public EnsureClassMethodBuilder ensureMethod() {
		return new EnsureClassMethodBuilder(this::ensure);
	}

	@NotNull
	public HasConstructorBuilder<EnsureClassConstructorOperation,AnalyzedClass, AnalyzedClass.ClassInsertionPoint> ensureConstructor() {
		return new HasConstructorBuilder<>(this::ensure,
			EnsureClassConstructorOperation::new);
	}

	@NotNull
	public WithType ensureField(@NotNull String name) {
		return type -> accessModifier -> {
			EnsureClassField operation = new EnsureClassField(name, type, accessModifier);
			ensure(operation);
			return operation;
		};
	}

	public interface WithType {
		@NotNull
		WithAccessModifier typed(@NotNull String type);
	}

	public interface WithAccessModifier {
		@NotNull
		EnsureField withAccess(@NotNull AccessModifier modifier);
	}

	public void ensureImplements(@NotNull String iface) {
		ensure(new EnsureImplements(iface));
	}

	public void ensureSuperclass(@NotNull String superClass) {
		ensure(new EnsureSuperClass(superClass));
	}


}
