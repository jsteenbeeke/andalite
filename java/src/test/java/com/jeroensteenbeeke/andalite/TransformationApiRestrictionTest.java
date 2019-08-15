package com.jeroensteenbeeke.andalite;

import com.jeroensteenbeeke.andalite.core.IReplaceable;
import com.jeroensteenbeeke.andalite.core.Transformation;
import com.jeroensteenbeeke.andalite.java.transformation.operations.IJavaOperation;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;

import static com.tngtech.archunit.lang.conditions.ArchConditions.callMethod;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class TransformationApiRestrictionTest {
	@Test
	public void testDirectInvocationsOfTransformation() {
		JavaClasses importedClasses = new ClassFileImporter().importPackages("com.jeroensteenbeeke.andalite.java.transformation.operations.impl");

		ArchRule rule = noClasses().that().implement(IJavaOperation.class)
									  .should(callMethod(
										  Transformation.class, "replace", IReplaceable.class, String.class
									  )).because("The replace method of IReplacable should be called instead");


		rule.check(importedClasses);
	}
}
