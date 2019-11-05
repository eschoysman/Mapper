package es.utils.mapper.factory.builder;

import java.util.function.Function;
import java.util.function.Supplier;

import es.utils.mapper.Mapper;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.impl.object.ClassMapper;

/**
 * Fourth step of the ElementMapper builder that manage the creation of a default value
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see ElementMapper
 * @see From
 * @see To
 */
public class Default<IN,GETTER_OUT,SETTER_IN,OUT> extends ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> {

	protected Default(Mapper mapper, ClassMapper<IN, OUT> mapping, Getter<IN,GETTER_OUT> getter,
																		Function<GETTER_OUT,SETTER_IN> transformer,
																		Setter<OUT,SETTER_IN> setter) {
		super(mapper,mapping,getter,transformer,setter,null);
	}

	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> defaultValue(SETTER_IN defaultValue) {
		return defaultValue(()->defaultValue);
	}
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> defaultValue(Supplier<SETTER_IN> defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> defaultValueFor(Class<SETTER_IN> defaultValueType) {
		this.defaultValue = mapper.config().getDefaultValueSupplier(defaultValueType);
		return this;
	}
//	 public ElementMapperBuilder<IN,GETTER_OUT,SETTER_IN,OUT> noDefaultValue() {
//	 	this.defaultValue = null;
//	 	return this;
//	 }
	
}
