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
import com.google.common.base.Joiner;
import com.google.common.collect.*;
import com.jeroensteenbeeke.andalite.core.IInsertionPoint;
import com.jeroensteenbeeke.andalite.core.IInsertionPointProvider;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

import javax.annotation.CheckForNull;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ContainingDenomination<T extends ContainingDenomination<T,I>, I extends Enum<I> & IInsertionPoint<T>> extends Denomination<T,I> implements
		Javadocable {
	private final Map<String, Denomination> innerDenominations;

	private final Multimap<String, AnalyzedMethod> methods;

	private final Map<String, AnalyzedField> fields;

	private final Set<GenerifiedName> interfaces;

	private Location lastImplementsLocation = null;

	// Containing denominations can be body-less. For instance enum-constants
	private Location bodyLocation = null;

	private String javadoc;

	protected ContainingDenomination(@NotNull AnalyzedSourceFile sourceFile,
									 @NotNull Location location,
									 @NotNull List<Modifier.Keyword> modifiers,
									 @NotNull String packageName,
									 @NotNull LocatedName<SimpleName> name) {
		super(sourceFile, location, modifiers, packageName, name);
		this.methods = LinkedHashMultimap.create();
		this.innerDenominations = Maps.newHashMap();
		this.fields = Maps.newHashMap();
		this.interfaces = Sets.newHashSet();
	}

	@NotNull
	public Optional<Location> getLastImplementsLocation() {
		return Optional.ofNullable(lastImplementsLocation);
	}

	void setLastImplementsLocation(@NotNull Location lastImplementsLocation) {
		this.lastImplementsLocation = lastImplementsLocation;
	}

	protected void addField(@NotNull AnalyzedField field) {
		this.fields.put(field.getName(), field);
	}

	protected void addMethod(@NotNull AnalyzedMethod method) {
		if (!methods.containsValue(method)) {
			this.methods.put(method.getName(), method);
		}
	}

	protected void addInnerDenomination(@NotNull Denomination innerDenomination) {
		this.innerDenominations.put(innerDenomination.getDenominationName(),
									innerDenomination);
	}

	public boolean hasInnerClass(@NotNull String name) {
		return hasDenominationOfType(name, AnalyzedClass.class);
	}

	public boolean hasInnerInterface(@NotNull String name) {
		return hasDenominationOfType(name, AnalyzedInterface.class);
	}

	public boolean hasInnerEnum(@NotNull String name) {
		return hasDenominationOfType(name, AnalyzedEnum.class);
	}

	public boolean hasInnerAnnotation(@NotNull String name) {
		return hasDenominationOfType(name, AnalyzedAnnotationType.class);
	}

	private boolean hasDenominationOfType(String name,
										  Class<? extends Denomination> denominationClass) {
		if (innerDenominations.containsKey(name)) {
			Denomination denomination = innerDenominations.get(name);

			return denomination != null
					&& denominationClass.isAssignableFrom(denomination
																  .getClass());
		}

		return false;
	}

	@CheckForNull
	public AnalyzedClass getInnerClass(@NotNull String name) {
		if (hasInnerClass(name)) {
			return (AnalyzedClass) innerDenominations.get(name);
		}

		return null;
	}

	@NotNull
	public List<AnalyzedField> getFields() {
		return ImmutableList.copyOf(fields.values());
	}

	public boolean hasField(@NotNull String name) {
		return fields.containsKey(name);
	}

	@CheckForNull
	public AnalyzedField getField(@NotNull String name) {
		if (hasField(name)) {
			return fields.get(name);
		}

		return null;
	}

	@NotNull
	public Set<GenerifiedName> getInterfaces() {
		return ImmutableSet.copyOf(interfaces);
	}

	protected void addInterface(@NotNull GenerifiedName interfaceName) {
		this.interfaces.add(interfaceName);
	}

	public boolean implementsInterface(@NotNull String interfaceName) {
		return this.interfaces.stream().map(GenerifiedName::getName).anyMatch(interfaceName::equals);
	}

	@NotNull
	public List<AnalyzedMethod> getMethods() {
		return ImmutableList.copyOf(methods.values());
	}

	public GetMethodBuilder getMethod() {
		return new GetMethodBuilder(this);
	}

	@NotNull
	public List<AnalyzedClass> getInnerClasses() {
		return ImmutableList.copyOf(innerDenominations
										.values()
										.stream()
										.filter(d -> d instanceof AnalyzedClass)
										.map(d -> (AnalyzedClass) d)
										.collect(Collectors.toList()));
	}

	@NotNull
	public List<AnalyzedInterface> getInnerInterfaces() {
		return ImmutableList.copyOf(innerDenominations
										.values()
										.stream()
										.filter(d -> d instanceof AnalyzedInterface)
										.map(d -> (AnalyzedInterface) d)
										.collect(Collectors.toList()));
	}

	@NotNull
	public List<AnalyzedEnum> getInnerEnums() {
		return ImmutableList.copyOf(innerDenominations
										.values()
										.stream()
										.filter(d -> d instanceof AnalyzedEnum)
										.map(d -> (AnalyzedEnum) d)
										.collect(Collectors.toList()));
	}

	@NotNull
	public List<AnalyzedAnnotationType> getInnerAnnotations() {
		return ImmutableList.copyOf(innerDenominations
										.values()
										.stream()
										.filter(d -> d instanceof AnalyzedAnnotationType)
										.map(d -> (AnalyzedAnnotationType) d)
										.collect(Collectors.toList()));
	}

	@NotNull
	public Optional<Location> getBodyLocation() {
		return Optional.ofNullable(bodyLocation);
	}

	public void setBodyLocation(@NotNull Location bodyLocation) {
		this.bodyLocation = bodyLocation;
	}

	protected void outputInterfaces(IOutputCallback callback) {
		if (!interfaces.isEmpty()) {
			callback.write(" implements ");
			callback.write(Joiner.on(", ").join(interfaces));
		}

	}

	@Override
	public Optional<String> getJavadoc() {
		return Optional.ofNullable(javadoc);
	}

	@Override
	public void setJavadoc(String javadoc) {
		this.javadoc = javadoc;
	}
}
