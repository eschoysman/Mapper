package es.utils.mapper.factory.builder;

import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.utils.ThrowingConsumer;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Last step of the builder (before the building one) that manage the creation of the setter operation.<br>
 * This step is mandatory.<br>
 * Previous mandatory step: {@link From}
 * Previous optional step: {@link DefaultInput}, {@link Transformer}, {@link DefaultOutput}.<br>
 * Next mandatory step: {@link Builder}
 * @author eschoysman
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see <a href="package-summary.html">builder package</a>
 */
public interface To<IN,GETTER_OUT,SETTER_IN,OUT> {

	/**
	 * Create a {@code Getter} instance using the given getter object
	 * @param setter the Setter instance to use
	 * @return the second step of the builder to add a transformer
	 * @throws NullPointerException if {@code setter} is null
	 * @see Builder
	 */
	public Builder<IN,GETTER_OUT,SETTER_IN,OUT> to(Setter<OUT,SETTER_IN> setter);
	
	/**
	 * Create a Default with the data passed to the builder
	 * @param idName the name identifier of the {@code getter}
	 * @param fieldName the name of the field to retrieve from the generic {@code OUT} type
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code idName} or {@code fieldName} is null
	 * @see Builder
	 */
	public Builder<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, String fieldName);
	
	/**
	 * Create a empty {@code Setter} instance
	 * @return the second step of the builder to add a transformer
	 * @see Builder
	 */
	public default Builder<IN,GETTER_OUT,SETTER_IN,OUT> toEmpty() {
		return this.to(Setter.empty());
	}
	
	/**
	 * Create a EMBuilder with the data passed to the builder
	 * @param idName the name identifier of the {@code setter}
	 * @param setter the setting operation of the setter
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code idName} or {@code setter} is null
	 * @see Builder
	 */
	public default Builder<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, BiConsumer<OUT,SETTER_IN> setter) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(setter);
		Setter<OUT,SETTER_IN> resultSetter = new Setter<OUT,SETTER_IN>(idName,setter);
		return to(resultSetter);
	}
	
	/**
	 * Create a EMBuilder with the data passed to the builder
	 * @param fieldName the name of the field to retrieve from the generic {@code OUT} type
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code fieldName} is null
	 * @see Builder
	 */
	public default Builder<IN,GETTER_OUT,SETTER_IN,OUT> to(String fieldName) {
		return to(fieldName,fieldName);
	}
	
	/**
	 * Create a {@code Setter} instance using the informations present in the {@code fieldHolder}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code fieldHolder} is null
	 * @see Builder
	 */
	public default Builder<IN,GETTER_OUT,SETTER_IN,OUT> to(FieldHolder fieldHolder) {
		return to(fieldHolder.getFieldName(),fieldHolder);
	}
	
	/**
	 * Create a {@code Setter} instance using the informations present in the {@code fieldHolder}
	 * @param idName the name identifier of the {@code setter}
	 * @param fieldHolder a instance having all the information of a {@code field}
	 * @return a ElementMapper, result of the builder
	 * @throws NullPointerException if {@code idName} or {@code fieldHolder} is null
	 * @see Builder
	 */
	public default Builder<IN,GETTER_OUT,SETTER_IN,OUT> to(String idName, FieldHolder fieldHolder) {
		Objects.requireNonNull(idName);
		Objects.requireNonNull(fieldHolder);
		Setter<OUT,SETTER_IN> setter = new Setter<>(idName,EMBuilder.createSetterFunction(fieldHolder.getField()));
		return to(setter);
	}

	/**
	 * Set a {@link Consumer} for the {@code SETTER_IN} value and set a empty setter
	 * @param consumer the consumer of the {@code SETTER_IN} value
	 * @return a ElementMapper, result of the builder
	 */
	public Builder<IN,GETTER_OUT,Void,OUT> consume(ThrowingConsumer<SETTER_IN> consumer);


}
