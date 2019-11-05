package es.utils.mapper.factory.builder;

import java.util.function.Function;
import java.util.function.Supplier;

import es.utils.mapper.Mapper;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.utils.ThrowingFunction;

/**
 * Fifth step of the ElementMapper builder that manage the creation of the ElementMapper itself
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

	protected Mapper mapper;
	protected ClassMapper<IN,OUT> mapping;
	protected Getter<IN,GETTER_OUT> getter;
	protected ThrowingFunction<GETTER_OUT,SETTER_IN> transformer;
	protected Setter<OUT,SETTER_IN> setter;
	protected Supplier<SETTER_IN> defaultValue;
	protected ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> elementMapper;
	
	protected ElementMapperBuilder(Mapper mapper, ClassMapper<IN,OUT> mapping, Getter<IN,GETTER_OUT> getter,
																	 Function<GETTER_OUT,SETTER_IN> transformer,
																	 Setter<OUT,SETTER_IN> setter,
																	 Supplier<SETTER_IN> defaultValue) {
		this.mapper = mapper;
		this.mapping = mapping;
		this.mapping = mapping;
		this.getter = getter;
		this.transformer = transformer::apply;
		this.setter = setter;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Returns the ElementMapper created by the builder.
	 * @return a ElementMapperInstance
	 * @see ElementMapper
	 */
	public ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> getElementMapper() {
		if(elementMapper==null) {
			elementMapper = new ElementMapper<>(mapper,getter,transformer,setter,defaultValue);
		}
		return elementMapper;
	}
	/**
	 * Add the newly created elementMapper to the inner ClassMapper instance and return the ClassMapper. 
	 * @return the original ClassMapper
	 * @see ClassMapper 
	 * @see ElementMapper
	 */
	public ClassMapper<IN,OUT> create() {
		mapping.addElementMapper(getElementMapper());
		return mapping;
	}
	
}
