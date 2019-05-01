package es.utils.mapper.factory;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import es.utils.mapper.getter.FieldGetter;
import es.utils.mapper.getter.FunctionGetter;
import es.utils.mapper.getter.Getter;
import es.utils.mapper.getter.SupplierGetter;
import es.utils.mapper.getter.ValueGetter;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.utils.MapperUtil;

/**
 * 
 * @author Emmanuel
 *
 */
public class GetterFactory {

	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, TMP value) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		return new ValueGetter<>(name,value);
	}
	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Supplier<TMP> value) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		return new SupplierGetter<>(name,value);
	}
	
	/**
	 * 
	 * @param name
	 * @param getter
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Function<T,TMP> getter) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(getter);
		return new FunctionGetter<>(name,getter);
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(Field field) {
		Objects.requireNonNull(field);
		return getter(field.getName(),field);
	}
	/**
	 * 
	 * @param name
	 * @param field
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Field field) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldGetter<>(name,field);
	}
	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(Class<T> clazz, String fieldName) {
		return getter(fieldName,clazz,fieldName);
	}
	/**
	 * 
	 * @param name
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Class<T> clazz, String fieldName) {
		Field field = MapperUtil.getField(clazz,fieldName);
		return getter(name,field);
	}

	/**
	 * 
	 * @param fieldHolder
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(FieldHolder fieldHolder) {
		Objects.requireNonNull(fieldHolder);
		return getter(fieldHolder.getFieldName(),fieldHolder);
	}
	/**
	 * 
	 * @param name
	 * @param fieldHolder
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, FieldHolder fieldHolder) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(fieldHolder);
		return new FieldGetter<>(name,fieldHolder.getField());
	}
	
}
