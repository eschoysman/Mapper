package es.utils.mapper.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import es.utils.mapper.configuration.Configuration;
import es.utils.mapper.defaultvalue.DefaultValueFactory;

/**
 * This annotation allows the mapper to supply a default value for the setters.<br>
 * Examples of use cases:
 * <ul>
 *		<li>Simple string value:
 *			<ul>{@code @Default("default value")}</ul>
 *		</li>
 *		<li>String value with custom charset (the {@code charset} field is used for all the cases where the {@code value} field is used):
 *			<ul>{@code @Default(value="default value", charset="UTF-8")}</ul>
 *		</li>
 *		<li>Integer number value:
 *			<ul>{@code @Default(number=42)}</ul>
 *		</li>
 *		<li>Decimal number value:
 *			<ul>{@code @Default(decimal=0.5)}</ul>
 *		</li>
 *		<li>Boolean value:
 *			<ul>{@code @Default(boolean=true)}</ul>
 *		</li>
 *		<li>Character value:
 *			<ul>{@code @Default(character='c')}</ul>
 *		</li>
 *		<li>Class type value:
 *			<ul>{@code @Default(type=MyType.class)}</ul>
 *		</li>
 *		<li>Class type to use to ask the {@link Configuration} for the correct supplier:
 *			<ul>{@code @Default(type=MyTypeToSupply.class)}</ul>
 *		</li>
 *		<li>Enum value:
 *			<ul>{@code @Default(enumType=TimeUnit.class,value="MILLISECONDS")}</ul>
 *		</li>
 *		<li>Custom supplier class (similar to a factory but does not require an input):
 *			<ul>{@code @Default(supplier=MySupplier.class)}</ul>
 *		</li>
 *		<li>Custom factory:
 *			<ul>{@code @Default(factory=MyDefaultValueFactory.class, value="input value")}</ul>
 *		</li>
 *		<li>Custom factory with parameters:
 *			<ul>{@code @Default(factory=MyDateDefaultValue.class, value="24/12/2010", parameters="DD/mm/YYYY")}</ul>
 *		</li>
 * </ul>
 * @author eschoysman
 */
//@Repeatable(Defaults.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface Default {

	String value() default "";

	long number() default 0;
	double decimal() default 0;
	boolean bool() default false;
	char character() default 0;
	Class<?> type() default Object.class;

	@SuppressWarnings("rawtypes")
	Class<? extends Enum> enumType() default Enum.class;

	@SuppressWarnings("rawtypes")
	Class<? extends Supplier> supplier() default Supplier.class;
	
	@SuppressWarnings("rawtypes")
	Class<? extends DefaultValueFactory> factory() default DefaultValueFactory.class;
	String[] parameters() default {};
	
	String charset() default "";
	
}
