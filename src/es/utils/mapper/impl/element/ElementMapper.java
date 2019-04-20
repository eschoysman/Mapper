package es.utils.mapper.impl.element;

import java.util.function.Function;

import es.utils.mapper.getter.Getter;
import es.utils.mapper.setter.Setter;

public class ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> {

	private Getter<IN,GETTER_OUT> getter;
	private Function<GETTER_OUT,SETTER_IN> transformer;
	private Setter<OUT,SETTER_IN> setter;
	
	public ElementMapper(Getter<IN,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<OUT,SETTER_IN> setter) {
		this.transformer = transformer;
		this.getter = getter;
		this.setter = setter;
	}
	
	public String getFromValue() {
		return getter.getName();
	}
	public String getDestValue() {
		return setter.getName();
	}

	public void apply(IN in, OUT out) {
//		System.out.println("Mapping from "+fromValue+" to "+destValue+":");
		GETTER_OUT getterResult = getter.apply(in);
//		System.out.println("getter result: "+getterResult);
		SETTER_IN transformed = transformer.apply(getterResult);
//		System.out.println("setter input: "+transformed);
		setter.apply(out,transformed);
	}
	
}
