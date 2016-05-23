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
package com.jeroensteenbeeke.andalite.java.analyzer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.andalite.core.IOutputCallback;
import com.jeroensteenbeeke.andalite.core.Location;

public abstract class ContainingDenomination extends Denomination implements
		Javadocable {
	private final Map<String, Denomination> innerDenominations;

	private final Multimap<String, AnalyzedMethod> methods;

	private final Map<String, AnalyzedField> fields;

	private final Set<String> interfaces;

	private Location lastImplementsLocation = null;

	private Location bodyLocation = null;

	private String javadoc;

	protected ContainingDenomination(Location location, int modifiers,
			String packageName, TerminalNode denominationName) {
		super(location, modifiers, packageName, denominationName);
		this.methods = LinkedHashMultimap.create();
		this.innerDenominations = Maps.newHashMap();
		this.fields = Maps.newHashMap();
		this.interfaces = Sets.newHashSet();
	}

	@CheckForNull
	public Location getLastImplementsLocation() {
		return lastImplementsLocation;
	}

	void setLastImplementsLocation(@Nonnull Location lastImplementsLocation) {
		this.lastImplementsLocation = lastImplementsLocation;
	}

	protected void addField(@Nonnull AnalyzedField field) {
		this.fields.put(field.getName(), field);
	}

	protected void addMethod(@Nonnull AnalyzedMethod method) {
		if (!methods.containsValue(method)) {
			this.methods.put(method.getName(), method);
		}
	}

	protected void addInnerDenomination(@Nonnull Denomination innerDenomination) {
		this.innerDenominations.put(innerDenomination.getDenominationName(),
				innerDenomination);
	}

	public boolean hasInnerClass(@Nonnull String name) {
		return hasDenominationOfType(name, AnalyzedClass.class);
	}

	public boolean hasInnerInterface(@Nonnull String name) {
		return hasDenominationOfType(name, AnalyzedInterface.class);
	}

	public boolean hasInnerEnum(@Nonnull String name) {
		return hasDenominationOfType(name, AnalyzedEnum.class);
	}

	public boolean hasInnerAnnotation(@Nonnull String name) {
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
	public AnalyzedClass getInnerClass(@Nonnull String name) {
		if (hasInnerClass(name)) {
			return (AnalyzedClass) innerDenominations.get(name);
		}

		return null;
	}

	@Nonnull
	public List<AnalyzedField> getFields() {
		return ImmutableList.copyOf(fields.values());
	}

	public boolean hasField(@Nonnull String name) {
		return fields.containsKey(name);
	}

	@CheckForNull
	public AnalyzedField getField(@Nonnull String name) {
		if (hasField(name)) {
			return fields.get(name);
		}

		return null;
	}

	@Nonnull
	public Set<String> getInterfaces() {
		return ImmutableSet.copyOf(interfaces);
	}

	protected void addInterface(@Nonnull String interfaceName) {
		this.interfaces.add(interfaceName);
	}

	public boolean implementsInterface(@Nonnull String interfaceName) {
		return this.interfaces.contains(interfaceName);
	}

	@Nonnull
	public List<AnalyzedMethod> getMethods() {
		return ImmutableList.copyOf(methods.values());
	}

	public GetMethodBuilder getMethod() {
		return new GetMethodBuilder(this);
	}

	@Nonnull
	public List<AnalyzedClass> getInnerClasses() {
		return ImmutableList.copyOf(Iterables.filter(
				innerDenominations.values(), AnalyzedClass.class));
	}

	@Nonnull
	public List<AnalyzedInterface> getInnerInterfaces() {
		return ImmutableList.copyOf(Iterables.filter(
				innerDenominations.values(), AnalyzedInterface.class));
	}

	@Nonnull
	public List<AnalyzedEnum> getInnerEnums() {
		return ImmutableList.copyOf(Iterables.filter(
				innerDenominations.values(), AnalyzedEnum.class));
	}

	@Nonnull
	public List<AnalyzedAnnotationType> getInnerAnnotations() {
		return ImmutableList.copyOf(Iterables.filter(
				innerDenominations.values(), AnalyzedAnnotationType.class));
	}

	@CheckForNull
	public Location getBodyLocation() {
		return bodyLocation;
	}

	public void setBodyLocation(@Nonnull Location bodyLocation) {
		this.bodyLocation = bodyLocation;
	}

	protected void outputInterfaces(IOutputCallback callback) {
		if (!interfaces.isEmpty()) {
			callback.write(" implements ");
			callback.write(Joiner.on(", ").join(interfaces));
		}

	}

	public boolean isAutoAbstractMethods() {
		return false;
	}

	@Override
	public String getJavadoc() {
		return javadoc;
	}

	@Override
	public void setJavadoc(String javadoc) {
		this.javadoc = javadoc;
	}
}
