package es.utils.mapper.factory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import es.utils.mapper.getter.Getter;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.setter.Setter;

/**
 * This class allow the user to create component for custom mappings
 * @author eschoysman
 * @see Getter
 * @see Setter
 * @see FieldHolder
 */
public class Factory {

    // COLLECTION
    /**
     * This method instantiate a new collection based on collectionType.
     * @param <IN> the type of the objects in the origin collection
     * @param <OUT> the type of the objects in the destination collection
     * @param inputCollectionType the class of input. It must extend Collection.
     * @param collectionType the class of input. It must extend Collection.
     * @return a new instance of collectionType.
     */
	@SuppressWarnings({ "rawtypes" })
	public static <IN,OUT> Collection<OUT> collection(Class<? extends Collection> inputCollectionType, Class<? extends Collection> collectionType) {
		return CollectionFactory.create(inputCollectionType,collectionType);
	}

	// ELEMENT
	/**
	 * Create a {@code ElementMapper}
	 * @param <IN> the type of the origin object
	 * @param <TMP> the type of the result of the {@code getter} operation and of the input of the {@code setter} operation
	 * @param <OUT> the type of the destination object
	 * @param fromValue the name identifier of the getter operation
	 * @param destValue the name identifier of the setter operation
	 * @param getter the getter operation
	 * @param setter the setter operation
	 * @return A {@code ElementMapper}
	 * @see ElementMapper
	 * @see Factory#element(Getter, Setter)
	 */
	public static <IN,TMP,OUT> ElementMapper<IN,TMP,TMP,OUT> element(String fromValue, String destValue, Function<IN,TMP> getter, BiConsumer<OUT,TMP> setter) {
		return ElementMapperFactory.create(fromValue,destValue,getter,setter);
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
	 * @see Factory#element(Getter, Function, Setter)
	 */
	public static <IN,GETTER_OUT,SETTER_IN,OUT> ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> element(String fromValue, String destValue, Function<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, BiConsumer<OUT,SETTER_IN> setter) {
		return ElementMapperFactory.create(fromValue,destValue,getter,transformer,setter);
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
	public static <IN,GETTER_OUT,SETTER_IN,OUT> ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> element(Getter<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		return ElementMapperFactory.create(getter,transformer,setter);
	}
	/**
	 * Create a {@code ElementMapper}
	 * @param <IN> the type of the origin object
	 * @param <TMP> the type of the result of the {@code getter} operation and of the input of the {@code setter} operation
	 * @param <OUT> the type of the destination object
	 * @param getter a {@code Getter} instance that contains the information needed to execute the {@code getter} operation
	 * @param setter a {@code Setter} instance that contains the information needed to execute the {@code setter} operation
	 * @return A {@code ElementMapper}
	 * @see ElementMapper
	 * @see Getter
	 * @see Setter
	 */
	public static <IN,TMP,OUT> ElementMapper<IN,TMP,TMP,OUT> element(Getter<IN,TMP> getter, Setter<OUT,TMP> setter) {
		return element(getter,Function.identity(),setter);
	}

	// GETTER
	/**
	 * Create a {@code Getter} istance with the given {@code name} that return always the same value
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value to be mapped
	 * @param name the name identifier of the {@code getter}
	 * @param value the constant value to be retrive
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code value} is null
	 * @see Getter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, TMP value) {
		return GetterFactory.getter(name,value);
	}
	/**
	 * Create a {@code Getter} istance with the given {@code name} that return the supplied value
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value to be mapped
	 * @param name the name identifier of the {@code getter}
	 * @param supplier a supplier for the value to retrive
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code supplier} is null
	 * @see Getter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Supplier<TMP> supplier) {
		return GetterFactory.getter(name,supplier);
	}
	/**
	 * Create a {@code Getter} istance with the given {@code name} and how to retrive the value
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value to be mapped
	 * @param name the name identifier of the {@code getter}
	 * @param getter a function that extract the value to get 
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code getter} is null
	 * @see Getter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Function<T,TMP> getter) {
		return GetterFactory.getter(name,getter);
	}
	/**
	 * Create a {@code Getter} istance using the {@code field} name and the {@code value} of the field
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param field a file istance used to get the {@code name} and the value needed for creating the {@code Getter} 
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code field} is null
	 * @see Getter
	 */
	public static <T,TMP> Getter<T,TMP> getter(Field field) {
		return GetterFactory.getter(field.getName(),field);
	}
	/**
	 * Create a {@code Getter} istance using the given {@code name} and the {@code value}
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code getter}
	 * @param field a file istance used to get the value needed for creating the {@code Getter} 
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code field} is null
	 * @see Getter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Field field) {
		return GetterFactory.getter(name,field);
	}
	/**
	 * Create a {@code Getter} istance using the field named {@code fieldName} inside the {@code clazz} type
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param clazz the class containing the field to use as getter
	 * @param fieldName the name of the field to retrieve from the {@code clazz} type
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code clazz} or {@code fieldName} is null
	 * @see Getter
	 */
	public static <T,TMP> Getter<T,TMP> getter(Class<T> clazz, String fieldName) {
		return GetterFactory.getter(fieldName,clazz,fieldName);
	}
	/**
	 * Create a {@code Getter} istance using the field named {@code fieldName} of the {@code clazz} type
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code getter}
	 * @param clazz the class containing the field to use as getter
	 * @param fieldName the name of the field to retrieve from the {@code clazz} type
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name}, {@code clazz} or {@code fieldName} is null
	 * @see Getter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Class<T> clazz, String fieldName) {
		return GetterFactory.getter(clazz,fieldName);
	}
	/**
	 * Create a {@code Getter} istance using the informations present in the {@code fieldHolder}
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code fieldHolder} is null
	 * @see Getter
	 * @see FieldHolder
	 */
	public static <T,TMP> Getter<T,TMP> getter(FieldHolder fieldHolder) {
		return GetterFactory.getter(fieldHolder.getFieldName(),fieldHolder);
	}
	/**
	 * Create a {@code Getter} istance using the informations present in the {@code fieldHolder}
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code getter}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code fieldHolder} is null
	 * @see Getter
	 * @see FieldHolder
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, FieldHolder fieldHolder) {
		return GetterFactory.getter(name,fieldHolder.getField());
	}
	
	// SETTERS
	/**
	 * @param <U> the type of the destination object
	 * @param <TMP> the type of the value to be setted
	 * Create a {@code Setter} instance using with the {@code name} and the {@code setter} operation
	 * @param name the name identifier of the {@code setter}
	 * @param setter the setting operation of the setter 
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code name} or {@code setter} is null
	 * @see Setter
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, BiConsumer<U,TMP> setter) {
		return SetterFactory.setter(name,setter);
	}
	/**
	 * Create a {@code Setter} instance using the {@code field} name and the {@code value} of the field
	 * @param <U> the type of the destination object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param field a file instance used to get the {@code name} and the value needed for creating the {@code Setter} 
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code field} is null
	 * @see Setter
	 */
	public static <U,TMP> Setter<U,TMP> setter(Field field) {
		return SetterFactory.setter(field);
	}
	/**
	 * Create a {@code Setter} instance using the given {@code name} and the {@code value}
	 * @param <U> the type of the destination object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code setter}
	 * @param field a file instance used to get the value needed for creating the {@code Setter} 
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code name} or {@code field} is null
	 * @see Setter
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, Field field) {
		return SetterFactory.setter(name,field);
	}
	/**
	 * Create a {@code Setter} instance using the field named {@code fieldName} inside the {@code clazz} type
	 * @param <U> the type of the destination object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param clazz the class containing the field to use as setter
	 * @param fieldName the name of the field to retrieve from the {@code clazz} type
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code clazz} or {@code fieldName} is null or if there is no filed named {@code filedName} in the {@code clazz} type
	 * @see Setter
	 */
	public static <U,TMP> Setter<U,TMP> setter(Class<U> clazz, String fieldName) {
		return SetterFactory.setter(clazz,fieldName);
	}
	/**
	 * Create a {@code Setter} instance using the field named {@code fieldName} of the {@code clazz} type
	 * @param <U> the type of the destination object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code setter}
	 * @param clazz the class containing the field to use as setter
	 * @param fieldName the name of the field to retrieve from the {@code clazz} type
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code name}, {@code clazz} or {@code fieldName} is null or if there is no filed named {@code filedName} in the {@code clazz} type
	 * @see Setter
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, Class<U> clazz, String fieldName) {
		return SetterFactory.setter(name,clazz,fieldName);
	}
	/**
	 * Create a {@code Setter} instance using the informations present in the {@code fieldHolder}
	 * @param <U> the type of the destination object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code fieldHolder} is null
	 * @see Setter
	 * @see FieldHolder
	 */
	public static <U,TMP> Setter<U,TMP> setter(FieldHolder fieldHolder) {
		return SetterFactory.setter(fieldHolder.getField().getName(),fieldHolder.getField());
	}
	/**
	 * Create a {@code Setter} instance using the informations present in the {@code fieldHolder}
	 * @param <U> the type of the destination object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code setter}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return the {@code Setter} created
	 * @throws NullPointerException if {@code name} or {@code fieldHolder} is null
	 * @see Setter
	 * @see FieldHolder
	 */
	public static <U,TMP> Setter<U,TMP> setter(String name, FieldHolder fieldHolder) {
		return SetterFactory.setter(name,fieldHolder.getField());
	}
	
}
