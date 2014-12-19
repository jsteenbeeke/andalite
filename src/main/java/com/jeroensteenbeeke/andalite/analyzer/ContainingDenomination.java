package com.jeroensteenbeeke.andalite.analyzer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.common.base.Joiner;
import com.google.common.collect.*;
import com.jeroensteenbeeke.andalite.Location;

public abstract class ContainingDenomination extends Denomination {
	private final Map<String, Denomination> innerDenominations;

	private final Multimap<String, AnalyzedMethod> methods;

	private final Map<String, AnalyzedField> fields;

	private final Set<String> interfaces;

	private Location lastImplementsLocation = null;

	private Location bodyLocation = null;

	protected ContainingDenomination(Location location, int modifiers,
			String packageName, String denominationName) {
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

	protected void outputInterfaces(OutputCallback callback) {
		if (!interfaces.isEmpty()) {
			callback.write(" implements ");
			callback.write(Joiner.on(", ").join(interfaces));
		}

	}

	public boolean isAutoAbstractMethods() {
		return false;
	}

}
