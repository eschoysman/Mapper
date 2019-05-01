package es.utils.mapper.impl.element;

import java.util.function.Function;

import es.utils.mapper.getter.Getter;
import es.utils.mapper.setter.Setter;

/**
 * 
 * @author Emmanuel
 *
 * @param <IN>
 * @param <GETTER_OUT>
 * @param <SETTER_IN>
 * @param <OUT>
 */
public class ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> {

	private Getter<IN,GETTER_OUT> getter;
	private Function<GETTER_OUT,SETTER_IN> transformer;
	private Setter<OUT,SETTER_IN> setter;
	
	/**
	 * 
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
	 * 
	 * @return
	 */
	public String getFromValue() {
		return getter.getName();
	}
	/**
	 * 
	 * @return
	 */
	public String getDestValue() {
		return setter.getName();
	}

	/**
	 * 
	 * @param in
	 * @param out
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
