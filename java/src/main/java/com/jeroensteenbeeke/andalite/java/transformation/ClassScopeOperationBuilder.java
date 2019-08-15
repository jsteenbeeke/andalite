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

import javax.annotation.Nonnull;

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

	@Nonnull
	public FieldOperationBuilder forField(String name) {
		return new FieldOperationBuilder(getCollector(), getNavigation(), name);
	}

	@Nonnull
	public ClassScopeOperationBuilder forInnerClass(String name) {
		return new ClassScopeOperationBuilder(getCollector(),
			new InnerClassNavigation<>(getNavigation(), name));
	}

	@Nonnull
	public AnnotatableOperationBuilder<AnalyzedClass> forAnnotation(
			String type) {
		return new AnnotatableOperationBuilder<>(getCollector(), getNavigation(), type);
	}

	@Nonnull
	public ClassMethodLocator forMethod() {
		return new ClassMethodLocator(getCollector(), getNavigation());
	}

	@Nonnull
	public ClassConstructorLocator forConstructor() {
		return new ClassConstructorLocator(getCollector(), getNavigation());
	}

	public void ensureAnnotation(@Nonnull String annotation) {
		ensure(new EnsureClassAnnotation(annotation));
	}

	@Nonnull
	public EnsureClassMethodBuilder ensureMethod() {
		return new EnsureClassMethodBuilder(this::ensure);
	}

	@Nonnull
	public HasConstructorBuilder<EnsureClassConstructorOperation,AnalyzedClass, AnalyzedClass.ClassInsertionPoint> ensureConstructor() {
		return new HasConstructorBuilder<>(this::ensure,
			EnsureClassConstructorOperation::new);
	}

	@Nonnull
	public WithType ensureField(@Nonnull String name) {
		return type -> accessModifier -> {
			EnsureClassField operation = new EnsureClassField(name, type, accessModifier);
			ensure(operation);
			return operation;
		};
	}

	public interface WithType {
		@Nonnull
		WithAccessModifier typed(@Nonnull String type);
	}

	public interface WithAccessModifier {
		@Nonnull
		EnsureField withAccess(@Nonnull AccessModifier modifier);
	}

	public void ensureImplements(@Nonnull String iface) {
		ensure(new EnsureImplements(iface));
	}

	public void ensureSuperclass(@Nonnull String superClass) {
		ensure(new EnsureSuperClass(superClass));
	}


}
