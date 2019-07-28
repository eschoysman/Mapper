package es.utils.mapper.factory.builder;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import es.utils.mapper.Mapper;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.object.ClassMapper;

/**
 * Second (optional) step of the ElementMapper builder that manage the creation of the transformer operation
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <OUT> the type of the destination object
 * @see ClassMapper#createElementMapper()
 * @see ElementMapper
 * @see From
 * @see To
 * @see ElementMapperBuilder
 */
public class Transformer<IN,GETTER_OUT,OUT> extends To<IN,GETTER_OUT,GETTER_OUT,OUT> {

	Transformer(Mapper mapper, ClassMapper<IN,OUT> mapping, Getter<IN,GETTER_OUT> getter) {
		super(mapper,mapping,getter,obj->obj);
	}

	/**
	 * Add a transformer between the getter and setter operations 
	 * @param <SETTER_IN> the input type of the setter operation
	 * @param transformer a function to map the result of the getter into the correct type for the setter
	 * @return the third step of the builder
	 */
	public <SETTER_IN> To<IN,GETTER_OUT,SETTER_IN,OUT> transform(Function<GETTER_OUT,SETTER_IN> transformer) {
		Objects.requireNonNull(transformer);
		return new To<>(mapper,mapping,getter,transformer);
	}
	/**
	 * Add a generator for the setter operation
	 * @param <SETTER_IN> the input type of the setter operation
	 * @param supplier a supplier that return the value to set
	 * @return the third step of the builder
	 */
	public <SETTER_IN> To<IN,GETTER_OUT,SETTER_IN,OUT> transform(Supplier<SETTER_IN> supplier) {
		Objects.requireNonNull(supplier);
		return new To<>(mapper,mapping,getter,$->supplier.get());
	}
	/**
	 * Add no transformer between the getter and setter operations
	 * @return the third step of the builder
	 */
	public To<IN,GETTER_OUT,GETTER_OUT,OUT> noTransform() {
		return new To<>(mapper,mapping,getter,obj->obj);
	}
	
	/**
	 * Set a {@link Consumer} for the {@code GETTER_OUT} value and set a empty setter
	 * @param consumer the consumer of the {@code GETTER_OUT} value
	 * @return a ElementMapper, result of the builder
	 */
	public ElementMapperBuilder<IN,GETTER_OUT,Void,OUT> consume(Consumer<GETTER_OUT> consumer) {
		return this.<Void>transform(obj->{consumer.accept(obj);return null;}).toEmpty();
	}
	
}
