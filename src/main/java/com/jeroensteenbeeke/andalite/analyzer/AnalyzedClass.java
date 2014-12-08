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

package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.collect.*;
import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedClass extends AccessModifiable {
	private final String packageName;

	private final String className;

	private final Multimap<String, AnalyzedMethod> methods;

	private final Map<String, AnalyzedField> fields;

	private final List<AnalyzedConstructor> constructors;

	private final Map<String, AnalyzedClass> innerClasses;

	private Location bodyLocation = null;

	private Location extendsLocation = null;

	private Location lastImplementsLocation = null;

	private String superClass = null;

	private Set<String> interfaces;

	public AnalyzedClass(@Nonnull Location location, int modifiers,
			@Nonnull String packageName, @Nonnull String className) {
		super(location, modifiers);
		this.packageName = packageName;
		this.className = className;
		this.methods = LinkedHashMultimap.create();
		this.innerClasses = Maps.newHashMap();
		this.fields = Maps.newHashMap();
		this.constructors = Lists.newArrayList();
		this.interfaces = Sets.newHashSet();
	}

	@CheckForNull
	public Location getExtendsLocation() {
		return extendsLocation;
	}

	void setExtendsLocation(@Nonnull Location extendsLocation) {
		this.extendsLocation = extendsLocation;
	}

	@CheckForNull
	public Location getLastImplementsLocation() {
		return lastImplementsLocation;
	}

	void setLastImplementsLocation(@Nonnull Location lastImplementsLocation) {
		this.lastImplementsLocation = lastImplementsLocation;
	}

	@Nonnull
	public Set<String> getInterfaces() {
		return ImmutableSet.copyOf(interfaces);
	}

	void addInterface(@Nonnull String interfaceName) {
		this.interfaces.add(interfaceName);
	}

	public boolean implementsInterface(@Nonnull String interfaceName) {
		return this.interfaces.contains(interfaceName);
	}

	@CheckForNull
	public String getSuperClass() {
		return superClass;
	}

	void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	@CheckForNull
	public Location getBodyLocation() {
		return bodyLocation;
	}

	public void setBodyLocation(@Nonnull Location bodyLocation) {
		this.bodyLocation = bodyLocation;
	}

	@Nonnull
	public String getPackageName() {
		return packageName;
	}

	@Nonnull
	public String getClassName() {
		return className;
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

	public boolean hasInnerClass(@Nonnull String name) {
		return innerClasses.containsKey(name);
	}

	@CheckForNull
	public AnalyzedClass getInnerClass(@Nonnull String name) {
		if (hasInnerClass(name)) {
			return innerClasses.get(name);
		}

		return null;
	}

	@Nonnull
	public List<AnalyzedMethod> getMethods() {
		return ImmutableList.copyOf(methods.values());
	}

	@Nonnull
	public List<AnalyzedConstructor> getConstructors() {
		return ImmutableList.copyOf(constructors);
	}

	@Nonnull
	public List<AnalyzedClass> getInnerClasses() {
		return ImmutableList.copyOf(innerClasses.values());
	}

	void addField(@Nonnull AnalyzedField field) {
		this.fields.put(field.getName(), field);
	}

	void addMethod(@Nonnull AnalyzedMethod method) {
		if (!methods.containsValue(method)) {
			this.methods.put(method.getName(), method);
		}
	}

	void addConstructor(@Nonnull AnalyzedConstructor constructor) {
		this.constructors.add(constructor);
	}

	void addInnerClass(@Nonnull AnalyzedClass innerClass) {
		this.innerClasses.put(innerClass.getClassName(), innerClass);
	}

	@Override
	public void onModifierOutputted(OutputCallback callback) {
		callback.write("class ");
		callback.write(className);
		callback.write(" {");
		callback.increaseIndentationLevel();
		callback.newline();
		for (AnalyzedField analyzedField : getFields()) {
			analyzedField.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedConstructor constructor : getConstructors()) {
			constructor.output(callback);
			callback.newline();
			callback.newline();
		}

		for (AnalyzedMethod analyzedMethod : getMethods()) {
			analyzedMethod.output(callback);
			callback.newline();
			callback.newline();
		}

		callback.decreaseIndentationLevel();
		callback.newline();
		callback.write("}");
		callback.newline();

	}

}
