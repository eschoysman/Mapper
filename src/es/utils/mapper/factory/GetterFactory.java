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
 * This class creates an Getter based on input values.
 * @author eschoysman
 * @see Getter
 * @see FunctionGetter
 * @see FieldGetter
 * @see SupplierGetter
 * @see ValueGetter
 * @see FieldHolder
 */
class GetterFactory {

	/**
	 * Create a {@code Getter} instance with the given {@code name} that return always the same value
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value to be mapped
	 * @param name the name identifier of the {@code getter}
	 * @param value the constant value to be retrive
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code value} is null
	 * @see Getter
	 * @see ValueGetter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, TMP value) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		return new ValueGetter<>(name,value);
	}

	/**
	 * Create a {@code Getter} instance with the given {@code name} that return the supplied value
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value to be mapped
	 * @param name the name identifier of the {@code getter}
	 * @param supplier a supplier for the value to retrive
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code supplier} is null
	 * @see Getter
	 * @see SupplierGetter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Supplier<TMP> supplier) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(supplier);
		return new SupplierGetter<>(name,supplier);
	}

	/**
	 * Create a {@code Getter} instance with the given {@code name} and how to retrive the value
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value to be mapped
	 * @param name the name identifier of the {@code getter}
	 * @param getter a function that extract the value to get 
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code getter} is null
	 * @see Getter
	 * @see FunctionGetter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Function<T,TMP> getter) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(getter);
		return new FunctionGetter<>(name,getter);
	}

	/**
	 * Create a {@code Getter} instance using the given {@code name} and the {@code value}
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code getter}
	 * @param field a file instance used to get the value needed for creating the {@code Getter} 
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name} or {@code field} is null
	 * @see Getter
	 * @see FieldGetter
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Field field) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(field);
		return new FieldGetter<>(name,field);
	}
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the {@code clazz} type
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param clazz the class containing the field to use as getter
	 * @param fieldName the name of the field to retrieve from the {@code clazz} type
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code clazz} or {@code fieldName} is null
	 * @see Getter
	 * @see MapperUtil
	 */
	public static <T,TMP> Getter<T,TMP> getter(Class<T> clazz, String fieldName) {
		return getter(fieldName,clazz,fieldName);
	}
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} of the {@code clazz} type
	 * @param <T> the type of the origin object
	 * @param <TMP> the type of the value inside of {@code field}
	 * @param name the name identifier of the {@code getter}
	 * @param clazz the class containing the field to use as getter
	 * @param fieldName the name of the field to retrieve from the {@code clazz} type
	 * @return the {@code Getter} created
	 * @throws NullPointerException if {@code name}, {@code clazz} or {@code fieldName} is null
	 * @see Getter
	 * @see MapperUtil
	 */
	public static <T,TMP> Getter<T,TMP> getter(String name, Class<T> clazz, String fieldName) {
		Field field = MapperUtil.getField(clazz,fieldName);
		return getter(name,field);
	}

	/**
	 * Create a {@code Getter} instance using the informations present in the {@code fieldHolder}
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
		Objects.requireNonNull(name);
		Objects.requireNonNull(fieldHolder);
		return new FieldGetter<>(name,fieldHolder.getField());
	}
	
}
