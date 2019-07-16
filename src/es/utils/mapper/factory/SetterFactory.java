package es.utils.mapper.factory;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.BiConsumer;

import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.setter.*;
import es.utils.mapper.utils.MapperUtil;

/**
 * This class creates an Setter based on output values.
 * @author eschoysman
 * @see Setter
 * @see FunctionSetter
 * @see FieldSetter
 * @see FieldHolder
 */
class SetterFactory {

	/**
	 * @param <U> the type of the destination object
	 * @param <SETTER_IN> the type of the value to be setted
	 * Create a {@code Setter} instance using with the {@code name} and the {@code setter} operation
	 * @param name the name identifier of the {@code setter}
	 * @param setter the setting operation of the setter 
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code name} or {@code setter} is null
	 * @see Setter
	 * @see FunctionSetter
	 */
	public static <U,SETTER_IN> Setter<U,SETTER_IN> setter(String name, BiConsumer<U,SETTER_IN> setter) {
		Objects.requireNonNull(setter);
		return new FunctionSetter<>(name,setter);
	}

	/**
	 * Create a {@code Setter} instance using the {@code field} name and the {@code value} of the field
	 * @param <U> the type of the destination object
	 * @param <SETTER_IN> the type of the value inside of {@code field}
	 * @param field a file instance used to get the {@code name} and the value needed for creating the {@code Setter} 
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code field} is null
	 * @see Setter
	 * @see FieldSetter
	 */
	public static <U,SETTER_IN> Setter<U,SETTER_IN> setter(Field field) {
		Objects.requireNonNull(field);
		return new FieldSetter<>(field);
	}
	/**
	 * Create a {@code Setter} instance using the given {@code name} and the {@code value}
	 * @param <U> the type of the destination object
	 * @param <SETTER_IN> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code setter}
	 * @param field a file instance used to get the value needed for creating the {@code Setter} 
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code name} or {@code field} is null
	 * @see Setter
	 * @see FieldSetter
	 */
	public static <U,SETTER_IN> Setter<U,SETTER_IN> setter(String name, Field field) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldSetter<>(name,field);
	}
	/**
	 * Create a {@code Setter} instance using the field named {@code fieldName} inside the {@code type} type
	 * @param <U> the type of the destination object
	 * @param <SETTER_IN> the type of the value inside of {@code field}
	 * @param type the class containing the field to use as setter
	 * @param fieldName the name of the field to retrieve from the {@code type} type
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code type} or {@code fieldName} is null or if there is no filed named {@code filedName} in the {@code type} type
	 * @see Setter
	 * @see MapperUtil
	 */
	public static <U,SETTER_IN> Setter<U,SETTER_IN> setter(Class<U> type, String fieldName) {
		Field field = MapperUtil.getField(type,fieldName);
		Objects.requireNonNull(field);
		return new FieldSetter<>(field);
	}
	/**
	 * Create a {@code Setter} instance using the field named {@code fieldName} of the {@code type} type
	 * @param <U> the type of the destination object
	 * @param <SETTER_IN> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code setter}
	 * @param type the class containing the field to use as setter
	 * @param fieldName the name of the field to retrieve from the {@code type} type
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code name}, {@code type} or {@code fieldName} is null or if there is no filed named {@code filedName} in the {@code type} type
	 * @see Setter
	 * @see MapperUtil
	 */
	public static <U,SETTER_IN> Setter<U,SETTER_IN> setter(String name, Class<U> type, String fieldName) {
		Field field = MapperUtil.getField(type,fieldName);
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldSetter<>(name,field);
	}

}
