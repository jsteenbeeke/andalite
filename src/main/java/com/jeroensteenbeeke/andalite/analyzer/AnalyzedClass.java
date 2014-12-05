package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.jeroensteenbeeke.andalite.Location;

public final class AnalyzedClass extends AccessModifiable {
	private final String packageName;

	private final String className;

	private final Multimap<String, AnalyzedMethod> methods;

	private final Map<String, AnalyzedField> fields;

	private final List<AnalyzedConstructor> constructors;

	private Location bodyLocation = null;

	public AnalyzedClass(@Nonnull Location location, int modifiers,
			@Nonnull String packageName, @Nonnull String className) {
		super(location, modifiers);
		this.packageName = packageName;
		this.className = className;
		this.methods = LinkedHashMultimap.create();
		this.fields = Maps.newHashMap();
		this.constructors = Lists.newArrayList();
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

	@Nonnull
	public List<AnalyzedMethod> getMethods() {
		return ImmutableList.copyOf(methods.values());
	}

	@Nonnull
	public List<AnalyzedConstructor> getConstructors() {
		return ImmutableList.copyOf(constructors);
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
