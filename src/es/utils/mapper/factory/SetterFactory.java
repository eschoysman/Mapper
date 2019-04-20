package es.utils.mapper.factory;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.BiConsumer;

import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.setter.FieldSetter;
import es.utils.mapper.setter.FunctionSetter;
import es.utils.mapper.setter.Setter;
import es.utils.mapper.utils.MapperUtil;

public class SetterFactory {

	public static <U,TMP> Setter<U,TMP> setter(String name, BiConsumer<U,TMP> setter) {
		Objects.requireNonNull(setter);
		return new FunctionSetter<>(name,setter);
	}

	public static <U,TMP> Setter<U,TMP> setter(Field field) {
		Objects.requireNonNull(field);
		return new FieldSetter<>(field);
	}
	public static <U,TMP> Setter<U,TMP> setter(String name, Field field) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldSetter<>(name,field);
	}
	public static <U,TMP> Setter<U,TMP> setter(Class<U> clazz, String fieldName) {
		Field field = MapperUtil.getField(clazz,fieldName);
		Objects.requireNonNull(field);
		return new FieldSetter<>(field);
	}
	public static <U,TMP> Setter<U,TMP> setter(String name, Class<U> clazz, String fieldName) {
		Field field = MapperUtil.getField(clazz,fieldName);
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldSetter<>(name,field);
	}

	public static <U,TMP> Setter<U,TMP> setter(FieldHolder fieldHolder) {
		Objects.requireNonNull(fieldHolder);
		return new FieldSetter<>(fieldHolder.getField());
	}
	public static <U,TMP> Setter<U,TMP> setter(String name, FieldHolder fieldHolder) {
		Objects.requireNonNull(fieldHolder);
		return new FieldSetter<>(name,fieldHolder.getField());
	}

}
