package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author Emmanuel
 *
 */
@Retention(RUNTIME)
@Target({FIELD,TYPE})
public @interface IgnoreField {
	/**
	 * 
	 */
	String[] value() default ""; 
}
