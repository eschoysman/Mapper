package es.utils.mapper.impl.element;

import java.util.function.Function;

import es.utils.mapper.getter.Getter;
import es.utils.mapper.setter.Setter;

/**
 * This class contains the informations and the logic needed the execute the mapping of an single element:
 * <ul>
 * <li>a <b>Getter</b>: a function tha take a {@code IN} type object</li>
 * <li>a <b>Transformer</b>: a function that maps the result of the {@code getter} into the correct type for the {@code setter}</li>
 * <li>a <b>Setter</b>: a operation that assign the result of the {@code transformer} to the destination object</li>
 * </ul>
 * 
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the type of the field (of the origin object) to be mapped into the destination object 
 * @param <SETTER_IN> the type of the field (of the destination object) mapped
 * @param <OUT> the type of the destination object
 * @see Getter
 * @see GetterFactory
 * @see Setter
 * @see SetterFactory
 */
public class ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> {

	private Getter<IN,GETTER_OUT> getter;
	private Function<GETTER_OUT,SETTER_IN> transformer;
	private Setter<OUT,SETTER_IN> setter;
	
	/**
	 * @param getter
	 * @param transformer
	 * @param setter
	 */
	public ElementMapper(Getter<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		this.transformer = transformer;
		this.getter = getter;
		this.setter = setter;
	}
	
	/**
	 * @return the name identifier of the getter
	 */
	public String getFromValue() {
		return getter.getName();
	}
	/**
	 * @return the name identifier of the setter
	 */
	public String getDestValue() {
		return setter.getName();
	}

	/**
	 * The logic of the mapping of the current element.
	 * @param in the original object
	 * @param out the destination object
	 */
	public void apply(IN in, OUT out) {
//		System.out.println("Mapping from "+fromValue+" to "+destValue+":");
		GETTER_OUT getterResult = getter.apply(in);
//		System.out.println("getter result: "+getterResult);
		SETTER_IN transformed = transformer.apply(getterResult);
//		System.out.println("setter input: "+transformed);
		setter.apply(out,transformed);
	}
	
}
