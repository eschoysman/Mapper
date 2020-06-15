package es.utils.mapper.factory.builder;

import es.utils.mapper.Mapper;
import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.exception.CustomException;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.utils.MapperUtil;
import es.utils.mapper.utils.ThrowingConsumer;
import es.utils.mapper.utils.ThrowingFunction;
import es.utils.mapper.utils.ThrowingPredicate;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Step builder implementation to help the use to create easily an {@link ElementMapper};
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see es.utils.mapper.factory.builder
 * @see ElementMapper
 */
public class EMBuilder<IN,GETTER_OUT,SETTER_IN,OUT> implements From		<IN,OUT>,					  // Mandatory
															DefaultInput<IN,GETTER_OUT,SETTER_IN,OUT>,// Optional
															Transformer	<IN,GETTER_OUT,SETTER_IN,OUT>,// Optional, Repeatable
															DefaultOutput<IN,GETTER_OUT,SETTER_IN,OUT>,// Optional
															To			<IN,GETTER_OUT,SETTER_IN,OUT>,// Mandatory
															Builder		<IN,GETTER_OUT,SETTER_IN,OUT> {// Mandatory

	private Mapper mapper;
	private ClassMapper<IN,OUT> mapping;
	private Getter<IN,GETTER_OUT> getter;
	private Supplier<GETTER_OUT> defaultInput;
	private ThrowingFunction<GETTER_OUT,SETTER_IN> transformer;
	private Supplier<SETTER_IN> defaultOutput;
	private Setter<OUT,SETTER_IN> setter;
	private ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> _elementMapper;
	
	private EMBuilder(Mapper mapper, ClassMapper<IN,OUT> mapping,
					Getter<IN,GETTER_OUT> getter,
					Supplier<GETTER_OUT> defaultInput,
					ThrowingFunction<GETTER_OUT, SETTER_IN> transformer,
					Supplier<SETTER_IN> defaultOutput,
					Setter<OUT,SETTER_IN> setter) {
		this.mapper = mapper;
		this.mapping = mapping;
		this.getter = getter;
		this.defaultInput = defaultInput;
		this.transformer = transformer;
		this.defaultOutput = defaultOutput;
		this.setter = setter;
	}

	// STARTING POINT
	/**
	 * Starting point of the step builder.
	 * @param mapper the belonging mapper of the mapping.
	 * @param mapping the belonging mapping of the resulting ElementMapper of this builder.
	 * @param <IN> the type of the origin object
	 * @param <OUT> the type of the destination object
	 * @return The first step of the builder.
	 */
	public static <IN,OUT> From<IN,OUT> using(Mapper mapper, ClassMapper<IN,OUT> mapping) {
		return new EMBuilder<>(mapper,mapping,null,null,null,null,null);
	}
	
	// GETTER (MANDATORY)
	public <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(Getter<IN,GETTER_OUT_NEW> getter) {
		Objects.requireNonNull(getter);
		return new EMBuilder<>(mapper,mapping,getter,null,null,null,null);
	}
	public <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(String idName, String fieldName) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldName);
		Field field = MapperUtil.getField(mapping.fromClass(),fieldName,mapper);
		Getter<IN,GETTER_OUT_NEW> getter = new Getter<>(idName,EMBuilder.createGetterFunction(field));
		return from(getter);
	}
	
	// DEFAULT VALUE IN INPUT (OPTIONAL)
	public Transformer<IN,GETTER_OUT,GETTER_OUT,OUT> defaultInput(Supplier<GETTER_OUT> defaultInput) {
		return new EMBuilder<>(mapper,mapping,getter,defaultInput,null,null,null);
	}
	public Transformer<IN,GETTER_OUT,GETTER_OUT,OUT> defaultInputFor(Class<GETTER_OUT> defaultValueType) {
		this.defaultInput = mapper.config().getDefaultValueSupplier(defaultValueType);
		return new EMBuilder<>(mapper,mapping,getter,defaultInput,null,null,null);
	}

	// TRANSFORM GETTER RESULT INTO SETTER INPUT (OPTIONAL,REPEATABLE)
	public <SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(ThrowingPredicate<SETTER_IN> condition, ThrowingFunction<SETTER_IN,SETTER_IN_NEW> transformerTrue, ThrowingFunction<SETTER_IN,SETTER_IN_NEW> transformerFalse) {
		ThrowingFunction<GETTER_OUT,SETTER_IN_NEW> currentTransform = null;
		if(this.transformer==null) {
			this.transformer = obj->(SETTER_IN)obj;
		}
		currentTransform = this.transformer.andThen(in->(condition.test(in)?transformerTrue:transformerFalse).apply(in))::apply;
		return new EMBuilder<>(mapper,mapping,getter,defaultInput,currentTransform,null,null);
	}
	public <SETTER_IN_NEW> Transformer<IN, GETTER_OUT, SETTER_IN_NEW, OUT> transform(Class<? extends AbstractConverter<SETTER_IN, SETTER_IN_NEW>> converter) throws MappingException {
		Objects.requireNonNull(converter);
		DirectMapper<SETTER_IN,SETTER_IN_NEW> converterInstance = MapperUtil.createFromConverter(converter,mapper);
		if(converterInstance==null) {
			throw CustomException.forType(MappingException.class).message(MessageFormat.format("Converter of {0} cannot be instantiate.",converter)).build();
		}
		return transform(converterInstance::mapOrNull);
	}

	// DEFAULT VALUE IN OUTPUT (OPTIONAL)
	public To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutput(Supplier<SETTER_IN> defaultOutput) {
		this.defaultOutput = defaultOutput;
		return this;
	}
	public To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutputFor(Class<SETTER_IN> defaultValueType) {
		return defaultOutput(mapper.config().getDefaultValueSupplier(defaultValueType));
	}

	// CONSUME (OPTIONAL)
	public Builder<IN,GETTER_OUT,Void,OUT> consume(ThrowingConsumer<SETTER_IN> consumer) {
		return this.<Void>transform(obj->{consumer.accept(obj);return null;}).toEmpty();
	}
	
	// SETTER (MANDATORY)
	public Builder<IN, GETTER_OUT, SETTER_IN, OUT> to(Setter<OUT, SETTER_IN> setter) {
		Objects.requireNonNull(setter);
		this.setter = setter;
		return this;
	}
	public Builder<IN, GETTER_OUT, SETTER_IN, OUT> to(String idName, String fieldName) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldName);
		Field field = MapperUtil.getField(mapping.toClass(),fieldName,mapper);
		Setter<OUT,SETTER_IN> setter = new Setter<>(idName,createSetterFunction(field));
		return to(setter);
	}
	
	// BUILDER (MANDATORY)
	public ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> getElementMapper() {
		if(_elementMapper==null) {
			if (this.defaultInput == null) {
				this.defaultInput = () -> null;
			}
			if (this.transformer == null) {
				@SuppressWarnings("unchecked")
				ThrowingFunction<GETTER_OUT, SETTER_IN> defaultTransform = obj -> (SETTER_IN)obj;
				this.transformer = defaultTransform;
			}
			if (this.defaultOutput == null) {
				this.defaultOutput = () -> null;
			}
			_elementMapper = new ElementMapper<>(mapper, getter, defaultInput, transformer, defaultOutput, setter);
		}
		return _elementMapper;
	}
	public ClassMapper<IN,OUT> create() {
		mapping.addElementMapper(getElementMapper());
		return mapping;
	}
	
	// UTILITY METHODS
	protected static <T,OUT> Function<T,OUT> createGetterFunction(Field field) {
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
	protected static <T,IN> BiConsumer<T,IN> createSetterFunction(Field field) {
		Objects.requireNonNull(field);
		field.setAccessible(true);
		return (obj,data) -> {
			try {
				field.set(obj,data);
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			}
		};
	}

}
