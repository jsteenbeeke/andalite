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
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureClassAnnotation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureField;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureImplements;
import com.jeroensteenbeeke.andalite.java.transformation.operations.impl.EnsureSuperClass;

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
				new InnerClassNavigation<AnalyzedClass>(getNavigation(), name));
	}

	@Nonnull
	public AnnotatableOperationBuilder<AnalyzedClass> forAnnotation(
			String type) {
		return new AnnotatableOperationBuilder<AnalyzedClass>(getCollector(),
				getNavigation(), type);
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
		return new EnsureClassMethodBuilder(o -> ensure(o));
	}

	@Nonnull
	public HasConstructorBuilder ensureConstructor() {
		return new HasConstructorBuilder(o -> ensure(o));
	}

	@Nonnull
	public HasFieldBuilderName ensureField(@Nonnull String name) {
		return new HasFieldBuilderName(name);
	}

	public void ensureImplements(@Nonnull String iface) {
		ensure(new EnsureImplements(iface));
	}

	public void ensureSuperclass(@Nonnull String superClass) {
		ensure(new EnsureSuperClass(superClass));
	}

	public class HasFieldBuilderName {
		private final String name;

		private HasFieldBuilderName(@Nonnull String name) {
			super();
			this.name = name;
		}

		public HasFieldBuilderNameType typed(@Nonnull String type) {
			return new HasFieldBuilderNameType(name, type);
		}
	}

	public class HasFieldBuilderNameType {
		private final String name;

		private final String type;

		private HasFieldBuilderNameType(@Nonnull String name,
				@Nonnull String type) {
			super();
			this.name = name;
			this.type = type;
		}

		public EnsureField withAccess(@Nonnull AccessModifier modifier) {
			EnsureField operation = new EnsureField(name, type, modifier);
			ensure(operation);
			return operation;
		}
	}
}
