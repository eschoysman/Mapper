package es.utils.mapper.factory.builder;

import es.utils.functionalinterfaces.throwing.FunctionX;
import es.utils.functionalinterfaces.throwing.PredicateX;
import es.utils.functionalinterfaces.throwing.SupplierX;
import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.exception.MappingException;

import java.util.Objects;

/**
 * Optional and repeatable step of the builder that allow to specify a transformer between the result of the getter and the input of the setter.<br>
 * Previous mandatory step: {@link From}.<br>
 * Previous optional step: {@link Name}, {@link DefaultInput}.<br>
 * Next optional step: {@link DefaultOutput}.<br>
 * Next mandatory step: {@link To}.
 * @author eschoysman
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see <a href="package-summary.html">builder package</a>
 */
public interface Transformer<IN,GETTER_OUT,SETTER_IN,OUT> extends DefaultOutput<IN,GETTER_OUT,SETTER_IN,OUT> {

	/**
	 * Add a transformer between the getter and setter operations.<br>
	 * Be careful of {@code NullPointerException}, if the input of the transformer is {@code null} a {@code NullPointerException} will be thrown.
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param transformer a function to map the result of the getter into the correct type for the setter
	 * @return The next (optional) step of the builder: {@link DefaultOutput}.
	 * @see DefaultOutput
	 * @see To
	 */
	default <SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(FunctionX<SETTER_IN, SETTER_IN_NEW> transformer) {
		return transform($->true,transformer,null);
	}
	
	/**
	 * Add a transformer between the getter and setter operations using the given converter class.<br>
	 * Be careful of {@code NullPointerException}, if the input of the transformer is {@code null} a {@code NullPointerException} will be thrown.
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param converter the class to instance to convert the the result of the getter into the correct type for the setter
	 * @return The next (optional) step of the builder: {@link DefaultOutput}.
	 * @throws MappingException if the given converter cannot be instantiate
	 * @see DefaultOutput
	 * @see To
	 */
	<SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(Class<? extends AbstractConverter<SETTER_IN, SETTER_IN_NEW>> converter) throws MappingException;

	/**
	 * Add a transformer between the getter and setter operations casting the value to the given type.<br>
	 * Be careful of {@code NullPointerException}, if the input of the transformer is {@code null} a {@code NullPointerException} will be thrown.
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param newType the class to cast the the result of the getter into the type for the setter
	 * @return The next (optional) step of the builder: {@link DefaultOutput}.
	 * @see DefaultOutput
	 * @see To
	 */
	default <SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> cast(Class<SETTER_IN_NEW> newType) {
		return transform(newType::cast);
	}

	/**
	 * If the condition on the input value is fulfilled, add a transformer between the getter and setter operations; otherwise does nothing.
	 * Be careful of {@code NullPointerException}, if the input of the transformer is {@code null} a {@code NullPointerException} will be thrown.
	 * @param condition the condition that must returns {@code true} in order to apply the given transformer
	 * @param transformerTrue the transformer that is apply only if the condition is respected
	 * @return The next (optional) step of the builder: {@link DefaultOutput}.
	 * @see DefaultOutput
	 * @see To
	 */
	default Transformer<IN,GETTER_OUT,SETTER_IN,OUT> transform(PredicateX<SETTER_IN> condition, FunctionX<SETTER_IN, SETTER_IN> transformerTrue) {
		return transform(condition,transformerTrue,$->$);
	}

	/**
	 * Add a transformer in depending of the result of the given condition
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param condition the condition that directs the transformer to apply
	 * @param transformerTrue the transformer that is apply if the condition returns {@code true}
	 * @param transformerFalse the transformer that is apply if the condition returns {@code false}
	 * @return The next (optional) step of the builder: {@link DefaultOutput}.
	 * @see DefaultOutput
	 * @see To
	 */
	<SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(PredicateX<SETTER_IN> condition, FunctionX<SETTER_IN, SETTER_IN_NEW> transformerTrue, FunctionX<SETTER_IN, SETTER_IN_NEW> transformerFalse);


	default Transformer<IN,GETTER_OUT,SETTER_IN,OUT> defaultValue(SupplierX<SETTER_IN> supplier) {
		return this.transform(Objects::isNull, in->supplier.get(), in->in);
	}
	default Transformer<IN,GETTER_OUT,SETTER_IN,OUT> defaultValue(SETTER_IN defaultValue) {
		return this.transform(Objects::isNull, in->defaultValue, in->in);
	}
	Transformer<IN,GETTER_OUT,SETTER_IN,OUT> defaultValueForType(Class<SETTER_IN> defaultValueType);

}
