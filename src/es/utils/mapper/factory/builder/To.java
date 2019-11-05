package es.utils.mapper.factory.builder;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import es.utils.mapper.Mapper;
import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.utils.MapperUtil;
import es.utils.mapper.utils.ThrowingConsumer;
import es.utils.mapper.utils.ThrowingFunction;

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
 * @see ElementMapperBuilder
 */
public class To<IN,GETTER_OUT,SETTER_IN,OUT> {
	
	protected Mapper mapper;
	protected ClassMapper<IN,OUT> mapping;
	protected Getter<IN,GETTER_OUT> getter;
	private ThrowingFunction<GETTER_OUT,SETTER_IN> transformer;
	
	To(Mapper mapper, ClassMapper<IN,OUT> mapping, Getter<IN, GETTER_OUT> getter, ThrowingFunction<GETTER_OUT, SETTER_IN> transformer) {
		this.mapper = mapper;
		this.mapping = mapping;
		this.getter = getter;
		this.transformer = transformer;
	}

	/**
	 * Add a transformer between the getter and setter operations 
	 * @param <SETTER_IN_NEW> the new input type of the setter operation
	 * @param transformer a function to map the result of the previous transformation into the correct type for the setter
	 * @return the third step of the builder
	 */
	public <SETTER_IN_NEW> To<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(ThrowingFunction<SETTER_IN,SETTER_IN_NEW> transformer) {
		Objects.requireNonNull(transformer);
		return new To<>(mapper,mapping,getter,this.transformer.andThen(transformer)::apply);
	}

	/**
	 * Add a transformer between the getter and setter operations using the given converter class
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param converter the class to instance to convert the the result of the getter into the correct type for the setter
	 * @return the third step of the builder
	 * @throws MappingException If the the given converter cannot be instantiate.
	 * @throws NullPointerException If the given {@code converter} is {@code null}
	 */
	public <SETTER_IN_NEW> To<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(Class<? extends AbstractConverter<SETTER_IN,SETTER_IN_NEW>> converter) throws MappingException {
		Objects.requireNonNull(converter);
		DirectMapper<SETTER_IN,SETTER_IN_NEW> converterInstance = MapperUtil.createFromConverter(converter,mapper);
		if(converterInstance==null) {
			throw new MappingException("Converter of "+converter+" cannot be istanziate.");
		}
		return new To<>(mapper,mapping,getter,this.transformer.andThen(converterInstance::mapOrNull)::apply);
	}
	
	/**
	 * Set a {@link Consumer} for the {@code SETTER_IN} value and set a empty setter
	 * @param consumer the consumer of the {@code SETTER_IN} value
	 * @return a ElementMapper, result of the builder
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,Void,OUT> consume(ThrowingConsumer<SETTER_IN> consumer) {
		return this.<Void>transform(obj->{consumer.accept(obj);return null;}).toEmpty();
	}
	
	
	/**
	 * Create a empty {@code Setter} instance
	 * @return the second step of the builder to add a transformer
	 * @see Getter
	 * @see Transformer
	 * @see DefaultValueBuild
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,Void,OUT> toEmpty() {
		@SuppressWarnings("unchecked")
		ElementMapperBuilder<IN,GETTER_OUT,Void,OUT> emb = (ElementMapperBuilder<IN,GETTER_OUT,Void,OUT>)this.to(Setter.empty());
		return emb;
	}
	
	/**
	 * Create a {@code Getter} instance using the given getter object
	 * @param setter the Setter instance to use
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code setter} is null
	 * @see Getter
	 * @see Transformer
	 * @see DefaultValueBuild
	 */
	public DefaultValueBuild<IN,GETTER_OUT,SETTER_IN,OUT> to(Setter<OUT,SETTER_IN> setter) {
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
	 * @see DefaultValueBuild
	 */
	public DefaultValueBuild<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, BiConsumer<OUT,SETTER_IN> setter) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(setter);
		Setter<OUT,SETTER_IN> resultSetter = new Setter<OUT,SETTER_IN>(idName,setter);
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
	 * @see DefaultValueBuild
	 */
	public DefaultValueBuild<IN,GETTER_OUT,SETTER_IN,OUT> to(String fieldName) {
		return to(fieldName,fieldName);
	}
	/**
	 * Create a DefaultValueBuild with the data passed to the builder
	 * @param idName the name identifier of the {@code getter}
	 * @param fieldName the name of the field to retrieve from the generic {@code OUT} type
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code idName} or {@code fieldName} is null
	 * @see Setter
	 * @see From
	 * @see Transformer
	 * @see DefaultValueBuild
	 */
	public DefaultValueBuild<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, String fieldName) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldName);
		Field field = MapperUtil.getField(mapping.toClass(),fieldName,mapper);
		Setter<OUT,SETTER_IN> setter = new Setter<>(idName,createSetterFunction(field));
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
	 * @see DefaultValueBuild
	 */
	public DefaultValueBuild<IN,GETTER_OUT,SETTER_IN,OUT> to(FieldHolder fieldHolder) {
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
	 * @see DefaultValueBuild
	 */
	public DefaultValueBuild<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, FieldHolder fieldHolder) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldHolder);
		Setter<OUT,SETTER_IN> setter = new Setter<>(idName,createSetterFunction(fieldHolder.getField()));
		return build(getter,transformer,setter);
	}
	
	// building operation
	private static <T,IN> BiConsumer<T,IN> createSetterFunction(Field field) {
		Objects.requireNonNull(field);
		field.setAccessible(true);
		return (obj,data) -> {
			try {
				field.set(obj,data);
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			}
		};
	}
	protected DefaultValueBuild<IN,GETTER_OUT,SETTER_IN,OUT> build(Getter<IN,GETTER_OUT> getter, ThrowingFunction<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		return new DefaultValueBuild<>(mapper,mapping,getter,transformer,setter);
	}
	
}
