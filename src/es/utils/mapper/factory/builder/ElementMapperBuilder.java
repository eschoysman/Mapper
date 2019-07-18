package es.utils.mapper.factory.builder;

import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.object.ClassMapper;

/**
 * Fourth step of the ElementMapper builder that manage the creation of the ElementMapper itself
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see ClassMapper#createElementMapper()
 * @see ElementMapper
 * @see From
 * @see To
 */
public class ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> {

	private ClassMapper<IN,OUT> mapping;
	private ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> elementMapper;
	
	ElementMapperBuilder(ClassMapper<IN,OUT> mapping, ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> elementMapper) {
		this.mapping = mapping;
		this.elementMapper = elementMapper;
	}

	/**
	 * Returns the ElementMapper created by the builder.
	 * @return a ElementMapperInstance
	 * @see ElementMapper
	 */
	public ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> getElementMapper() {
		return elementMapper;
	}
	/**
	 * Add the newly created elementMapper to the inner ClassMapper instance and return the ClassMapper. 
	 * @return the original ClassMapper
	 * @see ClassMapper 
	 * @see ElementMapper
	 */
	public ClassMapper<IN,OUT> build() {
		mapping.addElementMapper(elementMapper);
		return mapping;
	}
	
}
