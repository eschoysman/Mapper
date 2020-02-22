package es.utils.mapper.factory.builder;

import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.utils.ThrowingFunction;

/**
 * Optional and repeatable step of the EMBuilder that allow to specify a transformer between the result of the getter and the input of the setter.<br>
 * Previous step: {@link DefaultInput}<br>
 * Next step: {@link DefaultOutput}
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see <a href="package-summary.html">builder package</a>
 */
public interface Transformer<IN,GETTER_OUT,SETTER_IN,OUT> extends DefaultOutput<IN,GETTER_OUT,SETTER_IN,OUT> {

	/**
	 * Add a transformer between the getter and setter operations 
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param transformer a function to map the result of the getter into the correct type for the setter
	 * @return the next (optional) step of the builder: {@link Transformer}
	 */
	public <SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(ThrowingFunction<SETTER_IN,SETTER_IN_NEW> transformer);
	
	/**
	 * Add a transformer between the getter and setter operations using the given converter class
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param converter the class to instance to convert the the result of the getter into the correct type for the setter
	 * @return the next (optional) step of the builder: {@link Transformer}
	 * @throws MappingException if the given converter cannot be instantiate 
	 */
	public <SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> transform(Class<? extends AbstractConverter<SETTER_IN,SETTER_IN_NEW>> converter) throws MappingException;

	/**
	 * Add a transformer between the getter and setter operations casting the value to the given type.
	 * @param <SETTER_IN_NEW> the input type of the setter operation
	 * @param newType the class to cast the the result of the getter into the type for the setter
	 * @return the next (optional) step of the builder: {@link Transformer}
	 */
	public default <SETTER_IN_NEW> Transformer<IN,GETTER_OUT,SETTER_IN_NEW,OUT> cast(Class<SETTER_IN_NEW> newType) {
		return transform(newType::cast);
	}
	
}
