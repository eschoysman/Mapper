package es.utils.mapper.factory.builder;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import es.utils.mapper.Mapper;
import es.utils.mapper.getter.Getter;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.setter.FieldSetter;
import es.utils.mapper.setter.FunctionSetter;
import es.utils.mapper.setter.Setter;
import es.utils.mapper.utils.MapperUtil;

/**
 * Third step of the ElementMapper builder that manage the creation of the setter operation
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see ElementMapper
 * @see From
 * @see To
 * @see ElementMapperBuilder
 */
public class To<IN,GETTER_OUT,SETTER_IN,OUT> {
	
	protected Mapper mapper;
	protected ClassMapper<IN,OUT> mapping;
	protected Getter<IN,GETTER_OUT> getter;
	private Function<GETTER_OUT,SETTER_IN> transformer;
	
	To(Mapper mapper, ClassMapper<IN,OUT> mapping, Getter<IN, GETTER_OUT> getter, Function<GETTER_OUT, SETTER_IN> transformer) {
		this.mapper = mapper;
		this.mapping = mapping;
		this.getter = getter;
		this.transformer = transformer;
	}

	/**
	 * Create a {@code Getter} instance using the given getter object
	 * @param setter the Setter instance to use
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code setter} is null
	 * @see Getter
	 * @see Transformer
	 * @see To
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> to(Setter<OUT,SETTER_IN> setter) {
		Objects.requireNonNull(setter);
		return build(getter,transformer,setter);
	}
	/**
	 * Create a ElementMapperBuilder with the data passed to the builder
	 * @param idName the name identifier of the {@code setter}
	 * @param setter the setting operation of the setter 
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code idName} or {@code setter} is null
	 * @see Setter
	 * @see From
	 * @see Transformer
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, BiConsumer<OUT,SETTER_IN> setter) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(setter);
		Setter<OUT,SETTER_IN> resultSetter = new FunctionSetter<OUT,SETTER_IN>(idName,setter);
		return build(getter,transformer,resultSetter);
	}
	/**
	 * Create a ElementMapperBuilder with the data passed to the builder
	 * @param fieldName the name of the field to retrieve from the generic {@code OUT} type
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code fieldName} is null
	 * @see Setter
	 * @see From
	 * @see Transformer
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> to(String fieldName) {
		return to(fieldName,fieldName);
	}
	/**
	 * Create a ElementMapperBuilder with the data passed to the builder
	 * @param idName the name identifier of the {@code getter}
	 * @param fieldName the name of the field to retrieve from the generic {@code OUT} type
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code idName} or {@code fieldName} is null
	 * @see Setter
	 * @see From
	 * @see Transformer
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, String fieldName) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldName);
		Field field = MapperUtil.getField(mapping.toClass(),fieldName,mapper);
		Setter<OUT,SETTER_IN> setter = new FieldSetter<>(idName,field);
		return build(getter,transformer,setter);
	}
	/**
	 * Create a {@code Setter} instance using the informations present in the {@code fieldHolder}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code fieldHolder} is null
	 * @see Setter
	 * @see FieldHolder
	 * @see Transformer
	 * @see To
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> to(FieldHolder fieldHolder) {
		return to(fieldHolder.getFieldName(),fieldHolder);
	}
	/**
	 * Create a {@code Setter} instance using the informations present in the {@code fieldHolder}
	 * @param idName the name identifier of the {@code setter}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code idName} or {@code fieldHolder} is null
	 * @see Setter
	 * @see FieldHolder
	 * @see Transformer
	 * @see To
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, FieldHolder fieldHolder) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldHolder);
		Setter<OUT,SETTER_IN> setter = new FieldSetter<>(idName,fieldHolder.getField());
		return build(getter,transformer,setter);
	}
	
	// building operation
	private ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> build(Getter<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		return new ElementMapperBuilder<>(mapping, new ElementMapper<>(getter,transformer,setter));
	}
	
}
