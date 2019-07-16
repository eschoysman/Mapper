package es.utils.mapper.factory;

import java.util.function.BiConsumer;
import java.util.function.Function;

import es.utils.mapper.getter.Getter;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.setter.Setter;

import static es.utils.mapper.factory.GetterFactory.*;
import static es.utils.mapper.factory.SetterFactory.*;

/**
 * This class creates an ElementMapper based on input and output values.
 * @author eschoysman
 */
class ElementMapperFactory {

	/**
	 * Create a {@code ElementMapper}
	 * @param <IN> the type of the origin object
	 * @param <TYPE> the type of the result of the {@code getter} operation and of the input of the {@code setter} operation
	 * @param <OUT> the type of the destination object
	 * @param fromValue the name identifier of the getter operation
	 * @param destValue the name identifier of the setter operation
	 * @param getter the getter operation
	 * @param setter the setter operation
	 * @return A {@code ElementMapper}
	 * @see ElementMapper
	 * @see ElementMapperFactory#create(Getter, Setter)
	 */
	public static <IN,TYPE,OUT> ElementMapper<IN,TYPE,TYPE,OUT> create(String fromValue, String destValue, Function<IN,TYPE> getter, BiConsumer<OUT,TYPE> setter) {
		return create(getter(fromValue,getter),Function.identity(),setter(destValue,setter));
	}

	/**
	 * Create a {@code ElementMapper}
	 * @param <IN> the type of the origin object
	 * @param <GETTER_OUT> the type of the result of the {@code getter} operation
	 * @param <SETTER_IN> the type of the input of the {@code setter} operation
	 * @param <OUT> the type of the destination object
	 * @param fromValue the name identifier of the getter operation
	 * @param destValue the name identifier of the setter operation
	 * @param getter the getter operation
	 * @param transformer a function that maps the result of the {@code getter} into the correct type for the {@code setter}
	 * @param setter the setter operation
	 * @return A {@code ElementMapper}
	 * @see ElementMapper
	 * @see ElementMapperFactory#create(Getter, Function, Setter)
	 */
	public static <IN,GETTER_OUT,SETTER_IN,OUT> ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> create(String fromValue, String destValue, Function<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, BiConsumer<OUT,SETTER_IN> setter) {
		return create(getter(fromValue,getter),transformer,setter(destValue,setter));
	}

	/**
	 * Create a {@code ElementMapper}
	 * @param <IN> the type of the origin object
	 * @param <GETTER_OUT> the type of the result of the {@code getter} operation
	 * @param <SETTER_IN> the type of the input of the {@code setter} operation
	 * @param <OUT> the type of the destination object
	 * @param getter a {@code Getter} instance that contains the information needed to execute the {@code getter} operation
	 * @param transformer a function that maps the result of the {@code getter} into the correct type for the {@code setter}
	 * @param setter a {@code Setter} instance that contains the information needed to execute the {@code setter} operation
	 * @return A {@code ElementMapper}
	 * @see ElementMapper
	 * @see Getter
	 * @see Setter
	 */
	public static <IN,GETTER_OUT,SETTER_IN,OUT> ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> create(Getter<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		return new ElementMapper<>(getter,transformer,setter);
	}

}
