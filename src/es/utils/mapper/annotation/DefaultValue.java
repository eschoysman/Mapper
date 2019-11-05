package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import es.utils.mapper.configuration.Configuration;

/**
 * This annotation allows the mapper to supply a default value for the setter.
 * @author eschoysman
 * @see Configuration
 * @see Configuration#addDefaultValueSupplier(Class,Supplier)
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface DefaultValue {

	Class<?> value();
	
}
