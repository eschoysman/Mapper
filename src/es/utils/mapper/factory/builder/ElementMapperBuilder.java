package es.utils.mapper.factory.builder;

import java.util.function.Function;

import es.utils.mapper.Mapper;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.impl.object.ClassMapper;

/**
 * Fourth step of the ElementMapper builder that manage the creation of the ElementMapper itself
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see ClassMapper#addMapping()
 * @see ElementMapper
 * @see From
 * @see To
 */
public class ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> {

	private ClassMapper<IN,OUT> mapping;
	private ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> elementMapper;
	
	ElementMapperBuilder(Mapper mapper, ClassMapper<IN,OUT> mapping, Getter<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		this.mapping = mapping;
		this.elementMapper = new ElementMapper<>(mapper,getter,transformer,setter);
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
	public ClassMapper<IN,OUT> create() {
		mapping.addElementMapper(elementMapper);
		return mapping;
	}
	
}
