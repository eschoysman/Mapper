package es.utils.mapper.getter;

import java.util.function.Function;

public class FunctionGetter<T,TMP> extends Getter<T,TMP> {

	public FunctionGetter(String name, Function<T,TMP> getter) {
		super(name,getter);
	}

}
