package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Ignore field annotation lets you specify the fields that should be ignored during the mapping.
 * This annotation can be used on single field or on class, where you should specify the field you'd like
 * to ignore. An empty value on class indicates that NONE of the fields should be ignored.
 * @author eschoysman
 *
 */
@Retention(RUNTIME)
@Target({FIELD,TYPE})
public @interface IgnoreField {
	/**
	 * @return if the target is a {@code TYPE} the list of the field to be ignored, otherwise (target is a {@code FIELD}) an empty array
	 */
	String[] value() default "";
}
