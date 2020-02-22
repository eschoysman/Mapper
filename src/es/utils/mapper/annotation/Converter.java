package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import es.utils.mapper.converter.AbstractConverter;

/**
 * This annotation allows the mapper to map a field to another that doesn't have the same type using, if found, the most appropriate converter given.<br>
 * This annotation is repeatable. Multiple converters are useful when a single class is mapped on multiple source or destination.
 * @author eschoysman
 * @see AbstractConverter
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(Converters.class)
public @interface Converter {
	/**
	 * @return The converter type that could be used to map the annotated field.
	 */
	Class<? extends AbstractConverter<?,?>> value();
	
}
