package es.utils.mapper.factory.builder;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import es.utils.mapper.Mapper;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.utils.MapperUtil;

/**
 * First step of the ElementMapper builder that manage the creation of the getter operation
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <OUT> the type of the destination object
 * @see ClassMapper#createElementMapper()
 * @see ElementMapper
 * @see Transformer
 * @see To
 * @see ElementMapperBuilder
 */
public class From<IN,OUT> {
	
	private Mapper mapper;
	private ClassMapper<IN,OUT> mapping;

	From(Mapper mapper, ClassMapper<IN,OUT> mapping) {
		this.mapper = mapper;
		this.mapping = mapping;
	}
	
	/**
	 * Create a Builder for the creation of a {@link ElementMapper}
	 * @param <IN> the type of the origin object
	 * @param <OUT> the type of the destination object
	 * @param mapper the Mapper of belonging
	 * @param mapping the ClassMapper of belonging
	 * @return the first of three steps of the builder
	 * @see ElementMapper
	 * @see Transformer
	 * @see To
	 */
	public static <IN,OUT> From<IN,OUT> using(Mapper mapper, ClassMapper<IN,OUT> mapping) {
		Objects.requireNonNull(mapper);
		Objects.requireNonNull(mapping);
		return new From<>(mapper,mapping);
	}

	/**
	 * Create a empty {@code Getter} instance and manage the provide defaultValue as input of the setter
	 * @param <SETTER_IN> the input type of the setter operation
	 * @param defaultValue the value to be set
	 * @return the third step of the builder to add a setter
	 * @see To
	 */
	public <SETTER_IN> To<IN,Void,SETTER_IN,OUT> defaultValue(SETTER_IN defaultValue) {
		return this.<Void>fromEmpty().<SETTER_IN>transform(()->defaultValue);
	}
	
	/**
	 * Create a empty {@code Getter} instance
	 * @param <GETTER_OUT> the resulting type of the getter operation
	 * @return the second step of the builder to add a transformer
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> fromEmpty() {
		return from(Getter.empty());
	}
	
	/**
	 * Create a {@code Getter} instance using the given getter object
	 * @param <GETTER_OUT> the resulting type of the getter operation
	 * @param getter the Getter instance to use
	 * @return the second step of the builder to add a transformer
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> from(Getter<IN,GETTER_OUT> getter) {
		Objects.requireNonNull(getter);
		return new Transformer<>(mapper,mapping,getter);
	}
	/**
	 * Create a {@code Getter} instance with the given {@code idName} that return the supplied value
	 * @param <GETTER_OUT> the type of the value to be mapped
	 * @param idName the name identifier of the {@code getter}
	 * @param supplier a supplier for the value to retrieve
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code name} or {@code supplier} is null
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> from(String idName, Supplier<GETTER_OUT> supplier) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(supplier);
		Getter<IN,GETTER_OUT> getter = new Getter<IN,GETTER_OUT>(idName,$->supplier.get());
		return new Transformer<>(mapper,mapping,getter);
	}
	/**
	 * Create a {@code Getter} instance with the given {@code name} and how to retrieve the value
	 * @param <GETTER_OUT> the type of the value to be mapped
	 * @param idName the name identifier of the {@code getter}
	 * @param getter a function that extract the value to get
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code idName} or {@code getter} is null
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> from(String idName, Function<IN,GETTER_OUT> getter) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(getter);
		Getter<IN,GETTER_OUT> resultGetter = new Getter<IN,GETTER_OUT>(idName,getter);
		return new Transformer<>(mapper,mapping,resultGetter);
	}
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the generic {@code IN} type 
	 * @param <GETTER_OUT> the type of the value inside of {@code field}
	 * @param fieldName the name of the field to retrieve from the {@code type} type
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code type} or {@code fieldName} is null
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> from(String fieldName) {
		return from(fieldName,fieldName);
	}
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the generic {@code IN} type
	 * @param <GETTER_OUT> the type of the value inside of {@code field}
	 * @param idName the name identifier of the {@code getter}
	 * @param fieldName the name of the field to retrieve from the {@code type} type
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code idName}, {@code type} or {@code fieldName} is null
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> from(String idName, String fieldName) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldName);
		Field field = MapperUtil.getField(mapping.fromClass(),fieldName,mapper);
		Getter<IN,GETTER_OUT> getter = new Getter<>(idName,createGetterFunction(field));
		return new Transformer<>(mapper,mapping,getter);
	}
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the {@code type} type
	 * @param <GETTER_OUT> the type of the value inside of {@code field}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code fieldHolder} is null
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> from(FieldHolder fieldHolder) {
		Objects.requireNonNull(fieldHolder);
		return from(fieldHolder.getFieldName(),fieldHolder);
	}
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the {@code type} type
	 * @param <GETTER_OUT> the type of the value inside of {@code field}
	 * @param idName the name identifier of the {@code getter}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code idName} or {@code fieldHolder} is null
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public <GETTER_OUT> Transformer<IN,GETTER_OUT,OUT> from(String idName, FieldHolder fieldHolder) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldHolder);
		Getter<IN,GETTER_OUT> getter = new Getter<>(fieldHolder.getFieldName(),createGetterFunction(fieldHolder.getField()));
		return new Transformer<>(mapper,mapping,getter);
	}

	// private method
	private static <T,OUT> Function<T,OUT> createGetterFunction(Field field) {
		Objects.requireNonNull(field);
		field.setAccessible(true);
		return obj -> {
			OUT result = null;
			try {
				@SuppressWarnings("unchecked")
				OUT out = (OUT)field.get(obj);
				result = out;
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			}
			return result;
		};
	}
	
}
