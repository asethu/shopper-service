package com.instacart.shopper.rest.method;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;

/**
 * Indicates methods annotated with this will respond to HTTP PATCH requests.
 *
 * @author arun
 */
@Documented
@HttpMethod(value="PATCH")
@Target(value=ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface PATCH {

}
