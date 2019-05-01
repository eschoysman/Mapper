package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * 
 * @author Emmanuel
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface CollectionType {
	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends Collection> value();
}
