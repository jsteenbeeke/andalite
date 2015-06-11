package com.jeroensteenbeeke.andalite.xml;

public interface IOperationReceiver<T extends IXMLOperation> {
	void ensure(T operation);
}
