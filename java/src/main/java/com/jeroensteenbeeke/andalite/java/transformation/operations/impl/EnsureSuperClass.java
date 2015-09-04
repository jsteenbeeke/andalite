package com.jeroensteenbeeke.andalite.java.transformation.operations.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeroensteenbeeke.andalite.core.Location;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.core.exceptions.OperationException;
import com.jeroensteenbeeke.andalite.java.analyzer.AnalyzedClass;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IClassOperation;

public class EnsureSuperClass implements IClassOperation {
	private final String superClass;

	public EnsureSuperClass(String superClass) {
		this.superClass = superClass;
	}

	@Override
	public List<Transformation> perform(AnalyzedClass input)
			throws OperationException {
		if (superClass.equals(input.getSuperClass())) {
			return ImmutableList.of();
		} else {
			Location extendsLocation = input.getExtendsLocation();
			if (input.getSuperClass() != null && extendsLocation != null) {
				return ImmutableList.of(Transformation.replace(extendsLocation,
						superClass));
			} else {
				if (extendsLocation != null) {
					if (extendsLocation.getLength() == 0) {
						// Prefix with space
						return ImmutableList
								.of(Transformation.insertAfter(extendsLocation,
										" extends ".concat(superClass)));
					} else {
						return ImmutableList
								.of(Transformation.insertAfter(extendsLocation,
										"extends ".concat(superClass)));
					}
				} else {
					// Rare
					return ImmutableList.of(Transformation.insertAfter(
							input.getNameLocation(),
							" extends ".concat(superClass)));
				}
			}
		}
	}

	@Override
	public String getDescription() {
		return "ensure superclass is ".concat(superClass);
	}

}
