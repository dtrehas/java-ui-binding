package com.beirtipol.binding.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tags a class to be weaved by AspectJ with PropertySupport
 * 
 * @see PropertySupportAspect.aj
 * @author O041484
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ ElementType.TYPE })
public @interface BindableClass
{
}
