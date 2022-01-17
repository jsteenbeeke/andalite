package com.jeroensteenbeeke.andalite.analyzer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestType {
	String foo();

	/**
	 * The default bar limit
	 * @return Limit
	 */
	int bar() default 8;
}
