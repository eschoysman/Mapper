package es.utils.mapper.setter;

import java.util.function.BiConsumer;

/**
 * 
 * @author Emmanuel
 *
 * @param <U>
 * @param <TMP>
 */
public class FunctionSetter<U,TMP> extends Setter<U,TMP> {

	/**
	 * 
	 * @param name
	 * @param setter
	 */
	public FunctionSetter(String name, BiConsumer<U,TMP> setter) {
		super(name,setter);
	}

}
