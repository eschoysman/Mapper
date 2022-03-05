package es.utils.mapper.factory.builder;

import es.utils.functionalinterfaces.throwing.ConsumerX;

import java.util.function.Consumer;

/**
 * Last step of the EMBuilder (before the building one) that manage the consumer the value and does not set any value.<br>
 * This step is mandatory (Consume and {@link To} are mutually exclusive)
 * Previous step: {@link DefaultOutput}<br>
 * Next step: {@link Builder}
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see <a href="package-summary.html">builder package</a>
 */
public interface Consume<IN, GETTER_OUT, SETTER_IN, OUT> {

	/**
	 * Set a {@link Consumer} for the {@code SETTER_IN} value and set a empty setter
	 * @param consumer the consumer of the {@code SETTER_IN} value
	 * @return a ElementMapper, result of the builder
	 */
	public Builder<IN,GETTER_OUT,Void,OUT> consume(ConsumerX<SETTER_IN> consumer);
	
}
