package es.utils.mapper.factory;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.BiConsumer;

import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.setter.FieldSetter;
import es.utils.mapper.setter.FunctionSetter;
import es.utils.mapper.setter.Setter;
import es.utils.mapper.utils.MapperUtil;

/**
 * 
 * @author Emmanuel
 *
 */
public class SetterFactory {

	/**
	 * 
	 * @param name
	 * @param setter
	 * @return
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, BiConsumer<U,TMP> setter) {
		Objects.requireNonNull(setter);
		return new FunctionSetter<>(name,setter);
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public static <U,TMP> Setter<U,TMP> setter(Field field) {
		Objects.requireNonNull(field);
		return new FieldSetter<>(field);
	}
	/**
	 * 
	 * @param name
	 * @param field
	 * @return
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, Field field) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldSetter<>(name,field);
	}
	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static <U,TMP> Setter<U,TMP> setter(Class<U> clazz, String fieldName) {
		Field field = MapperUtil.getField(clazz,fieldName);
		Objects.requireNonNull(field);
		return new FieldSetter<>(field);
	}
	/**
	 * 
	 * @param name
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, Class<U> clazz, String fieldName) {
		Field field = MapperUtil.getField(clazz,fieldName);
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldSetter<>(name,field);
	}

	/**
	 * 
	 * @param fieldHolder
	 * @return
	 */
	public static <U,TMP> Setter<U,TMP> setter(FieldHolder fieldHolder) {
		Objects.requireNonNull(fieldHolder);
		return new FieldSetter<>(fieldHolder.getField());
	}
	/**
	 * 
	 * @param name
	 * @param fieldHolder
	 * @return
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, FieldHolder fieldHolder) {
		Objects.requireNonNull(fieldHolder);
		return new FieldSetter<>(name,fieldHolder.getField());
	}

}
