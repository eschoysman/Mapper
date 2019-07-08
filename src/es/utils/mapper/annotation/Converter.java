package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import es.utils.mapper.converter.AbstractConverter;

/**
 * This annotation allows the mapper to map a field to another that doesn't have the same type using, if found, the most appropriate converter given.
 * Multiple converters are useful when a single class is mapped on multiple source or destination.
 * <br>
 * How to use:
 * <pre>@Converter(MyConverter.class)</pre>
 * or
 * <pre>@Converter({MyFirstConverter.class,MySecondConverter.class})</pre>
 * @author eschoysman
 * @see AbstractConverter
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Converter {
	/**
	 * @return the list of the converters that could be used to map the annotated field
	 */
	Class<? extends AbstractConverter<?,?>>[] value() default {};
}
