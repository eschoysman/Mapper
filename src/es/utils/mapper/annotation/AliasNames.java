package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author Emmanuel
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface AliasNames {
	/**
	 * The list of the aliases allowed for this field
	 */
	String[] value();
}
