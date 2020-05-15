package es.utils.mapper.factory.builder;

import es.utils.mapper.Mapper;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.utils.ThrowingFunction;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * First step of the EMBuilder that manage the creation of the getter operation.<br>
 * This step is mandatory
 * Next step: {@link DefaultInput}
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <OUT> the type of the destination object
 * @see ClassMapper#addMapping()
 * @see EMBuilder#using(Mapper,ClassMapper)
 * @see ElementMapper
 * @see <a href="package-summary.html">builder package</a>
 */
public interface From<IN,OUT> {
	
	/**
	 * Create a {@code Getter} instance using the given getter object.
	 * @param <GETTER_OUT_NEW> the resulting type of the getter operation.
	 * @param getter the Getter instance to use.
	 * @return the second step of the builder to add a transformer.
	 * @see DefaultInput
	 */
	public <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(Getter<IN,GETTER_OUT_NEW> getter);

	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the generic {@code IN} type.
	 * @param <GETTER_OUT_NEW> the type of the value inside of {@code field}.
	 * @param idName the name identifier of the {@code getter}.
	 * @param fieldName the name of the field to retrieve from the {@code type} type.
	 * @return the second step of the builder to add a transformer.
	 * @throws NullPointerException if {@code idName}, {@code type} or {@code fieldName} is null.
	 * @see DefaultInput
	 */
	public <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(String idName, String fieldName);
	
	
	// EmptyFrom
	/**
	 * Create a empty {@code Getter} instance and manage the provide defaultValue as input of the setter.
	 * @param <SETTER_IN_NEW> the input type of the setter operation.
	 * @param defaultValue the value to be set.
	 * @return the third step of the builder to add a setter.
	 * @see To
	 */
	public default <SETTER_IN_NEW> To<IN,Void,SETTER_IN_NEW,OUT> defaultOutputValue(SETTER_IN_NEW defaultValue) {
		@SuppressWarnings("unchecked")
		ThrowingFunction<Void,SETTER_IN_NEW> transformer = obj->(SETTER_IN_NEW)obj;
		return this.<Void>fromEmpty().<SETTER_IN_NEW>transform(transformer).defaultOutput(defaultValue);
	}
	
	/**
	 * Create a empty {@code Getter} instance and manage the provide setter.
	 * @param <SETTER_IN_NEW> the input type of the setter operation.
	 * @param idName the name identifier of the {@code setter}.
	 * @param setter the setting operation of the setter.
	 * @return a ElementMapper, result of the builder.
	 * @throws NullPointerException if {@code idName} or {@code setter} is null.
	 * @see Builder
	 */
	public default <SETTER_IN_NEW> Builder<IN,Void,SETTER_IN_NEW,OUT> defaultOutput(String idName, BiConsumer<OUT,SETTER_IN_NEW> setter) {
		return this.<SETTER_IN_NEW>defaultOutputValue(null).to(idName,setter);
	}
	
	/**
	 * Create a empty {@code Getter} instance.
	 * @param <GETTER_OUT_NEW> the resulting type of the getter operation.
	 * @return the second step of the builder to add a transformer.
	 * @see DefaultInput
	 */
	public default <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> fromEmpty() {
		return from(Getter.empty());
	}
	
	
	
	/**
	 * Create a {@code Getter} instance with the given {@code idName} that return the supplied value.
	 * @param <GETTER_OUT_NEW> the type of the value to be mapped.
	 * @param idName the name identifier of the {@code getter}.
	 * @param supplier a supplier for the value to retrieve.
	 * @return the second step of the builder to add a transformer.
	 * @throws NullPointerException if {@code name} or {@code supplier} is null.
	 * @see DefaultInput
	 */
	public default <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(String idName, Supplier<GETTER_OUT_NEW> supplier) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(supplier);
		Getter<IN,GETTER_OUT_NEW> getter = new Getter<IN,GETTER_OUT_NEW>(idName,$->supplier.get());
		return from(getter);
	}
	
	/**
	 * Create a {@code Getter} instance with the given {@code name} and how to retrieve the value.
	 * @param <GETTER_OUT_NEW> the type of the value to be mapped.
	 * @param idName the name identifier of the {@code getter}.
	 * @param getter a function that extract the value to get.
	 * @return the second step of the builder to add a transformer.
	 * @throws NullPointerException if {@code idName} or {@code getter} is null.
	 * @see DefaultInput
	 */
	public default <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(String idName, ThrowingFunction<IN,GETTER_OUT_NEW> getter) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(getter);
		Getter<IN,GETTER_OUT_NEW> resultGetter = new Getter<IN,GETTER_OUT_NEW>(idName,getter);
		return from(resultGetter);
	}
	
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the generic {@code IN} type.
	 * @param <GETTER_OUT_NEW> the type of the value inside of {@code field}.
	 * @param fieldName the name of the field to retrieve from the {@code type} type.
	 * @return the second step of the builder to add a transformer.
	 * @throws NullPointerException if {@code type} or {@code fieldName} is null.
	 * @see DefaultInput
	 */
	public default <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(String fieldName) {
		return from(fieldName,fieldName);
	}
	
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the {@code type} type.
	 * @param <GETTER_OUT_NEW> the type of the value inside of {@code field}.
	 * @param fieldHolder a instance having all the information of a {@code field}.
	 * @return the second step of the builder to add a transformer.
	 * @throws NullPointerException if {@code fieldHolder} is null.
	 * @see DefaultInput
	 */
	public default <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(FieldHolder fieldHolder) {
		Objects.requireNonNull(fieldHolder);
		return from(fieldHolder.getFieldName(),fieldHolder);
	}
	
	/**
	 * Create a {@code Getter} instance using the field named {@code fieldName} inside the {@code type} type.
	 * @param <GETTER_OUT_NEW> the type of the value inside of {@code field}.
	 * @param idName the name identifier of the {@code getter}.
	 * @param fieldHolder a instance having all the information of a {@code field}.
	 * @return the second step of the builder to add a transformer.
	 * @throws NullPointerException if {@code idName} or {@code fieldHolder} is null.
	 * @see DefaultInput
	 */
	public default <GETTER_OUT_NEW> DefaultInput<IN,GETTER_OUT_NEW,GETTER_OUT_NEW,OUT> from(String idName, FieldHolder fieldHolder) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldHolder);
		Getter<IN,GETTER_OUT_NEW> getter = new Getter<>(fieldHolder.getFieldName(),EMBuilder.createGetterFunction(fieldHolder.getField()));
		return from(getter);
	}
	
}
