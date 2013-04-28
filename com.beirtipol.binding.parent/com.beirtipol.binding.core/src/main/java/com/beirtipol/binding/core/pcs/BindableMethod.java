package com.beirtipol.binding.core.pcs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindableMethod {
	String fieldName();

	Type type() default Type.SET;

	public static enum Type {
		GET, SET;
	}
}
