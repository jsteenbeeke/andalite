package com.jeroensteenbeeke.andalite.transformation.operations;

import java.util.List;

import com.jeroensteenbeeke.andalite.analyzer.Outputable;
import com.jeroensteenbeeke.andalite.transformation.Transformation;

public interface Operation<T extends Outputable> {
	List<Transformation> perform(T input);
}
