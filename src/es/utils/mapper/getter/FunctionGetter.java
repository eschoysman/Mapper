package es.utils.mapper.getter;

import java.util.function.Function;

/**
 * 
 * @author Emmanuel
 *
 * @param <T>
 * @param <TMP>
 */
public class FunctionGetter<T,TMP> extends Getter<T,TMP> {

	/**
	 * 
	 * @param name
	 * @param getter
	 */
	public FunctionGetter(String name, Function<T,TMP> getter) {
		super(name,getter);
	}

}
