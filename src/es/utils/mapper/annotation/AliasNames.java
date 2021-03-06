package es.utils.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * AliasNames allow the mapper to map a field to another that doesn't have the same name.
 * You can use this annotation to list the aliases allowed for this field. 
 * Multiple aliases are useful when a single class is mapped on multiple source or destination.
 * @author eschoysman
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface AliasNames {
	/**
	 * @return the list of the aliases names allowed for this field
	 */
	String[] value();
}
