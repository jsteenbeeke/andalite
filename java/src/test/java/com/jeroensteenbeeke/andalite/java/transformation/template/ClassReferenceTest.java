package com.jeroensteenbeeke.andalite.java.transformation.template;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ClassReferenceTest {
	@Test
	public void testReference() {
		ClassReference object = ClassReference.of("java.lang.Object");

		assertThat(object.prefix(), is("java.lang"));
		assertThat(object.name(), is("Object"));

		ClassReference widget = ClassReference.of("Widget");

		assertThat(widget.prefix(), is(""));
		assertThat(widget.name(), is("Widget"));

		ClassReference thisTest = ClassReference.of("com.jeroensteenbeeke.andalite.java.transformation.template.ClassReferenceTest");
		assertThat(thisTest.prefix(), is("com.jeroensteenbeeke.andalite.java.transformation.template"));
		assertThat(thisTest.name(), is("ClassReferenceTest"));
	}
}
