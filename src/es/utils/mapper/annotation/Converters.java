package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import es.utils.mapper.converter.AbstractConverter;

/**
 * This annotation is used to make the {@code @Converter} annotation repeatable.
 * @author eschoysman
 * @see Converter
 * @see AbstractConverter
 */
@Retention(RUNTIME)
@Target(FIELD)
@interface Converters {
	/**
	 * @return the list of the converters that could be used to map the annotated field
	 */
	Converter[] value();
}
