package com.jeroensteenbeeke.andalite.xml;

import com.jeroensteenbeeke.andalite.xml.operations.EnsureAttribute;
import com.jeroensteenbeeke.andalite.xml.operations.EnsureElement;

public final class XMLOperations {
	public static EnsureAttribute.Builder hasAttribute(String name) {
		return new EnsureAttribute.Builder(name);
	}

	public static EnsureElement.Builder hasElement(String name) {
		return new EnsureElement.Builder(name);
	}
}
