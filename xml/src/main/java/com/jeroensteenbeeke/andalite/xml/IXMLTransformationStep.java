package com.jeroensteenbeeke.andalite.xml;

import org.w3c.dom.Node;

public interface IXMLTransformationStep<T extends Node> {
	void transform(T node);
}
