package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Collection type annotation allows you to specify the concrete class the mapper should instantiate
 * the mapping element to.
 * @author eschoysman
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface CollectionType {
	/**
	 * @return the class type that will be used for creating the sub-collection type field.
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends Collection> value();
}
