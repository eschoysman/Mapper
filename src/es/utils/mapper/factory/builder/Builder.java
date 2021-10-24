package es.utils.mapper.factory.builder;

import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.object.ClassMapper;

/**
 * Last step of the builder that manage the creation of the getter operation.<br>
 * This step is mandatory
 * Previous step: {@link To}
 * @author eschoysman
 * @param <IN> the type of the origin object.
 * @param <GETTER_OUT> the resulting type of the getter operation.
 * @param <SETTER_IN> the input type of the setter operation.
 * @param <OUT> the type of the destination object.
 * @see ElementMapper
 * @see <a href="package-summary.html">builder package</a>
 */
public interface Builder<IN,GETTER_OUT,SETTER_IN,OUT> {

	/**
	 * Returns the ElementMapper created by the builder.
	 * @return a ElementMapper instance
	 * @see ElementMapper
	 */
	public ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> getElementMapper();
	/**
	 * Add the newly created elementMapper to the inner ClassMapper instance and return the ClassMapper. 
	 * @return the original ClassMapper.
	 * @see ClassMapper 
	 * @see ElementMapper
	 */
	public ClassMapper<IN,OUT> create();
	
}
