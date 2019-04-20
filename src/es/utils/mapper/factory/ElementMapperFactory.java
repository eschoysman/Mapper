package es.utils.mapper.factory;

import java.util.function.BiConsumer;
import java.util.function.Function;

import es.utils.mapper.getter.Getter;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.setter.Setter;

import static es.utils.mapper.factory.GetterFactory.*;
import static es.utils.mapper.factory.SetterFactory.*;

public class ElementMapperFactory {

	public static <IN,TMP,OUT> ElementMapper<IN,TMP,TMP,OUT> create(String fromValue, String destValue, Function<IN,TMP> getter, BiConsumer<OUT,TMP> setter) {
		return create(getter(fromValue,getter),Function.identity(),setter(destValue,setter));
	}

	public static <IN,GETTER_OUT,SETTER_IN,OUT> ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> create(String fromValue, String destValue, Function<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, BiConsumer<OUT,SETTER_IN> setter) {
		return create(getter(fromValue,getter),transformer,setter(destValue,setter));
	}

	public static <IN,GETTER_OUT,SETTER_IN,OUT> ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> create(Getter<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		return new ElementMapper<>(getter,transformer,setter);
	}

	public static <IN,TMP,OUT> ElementMapper<IN,TMP,TMP,OUT> create(Getter<IN,TMP> getter, Setter<OUT,TMP> setter) {
		return create(getter,Function.identity(),setter);
	}

}
