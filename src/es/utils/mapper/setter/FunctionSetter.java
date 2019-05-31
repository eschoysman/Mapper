package es.utils.mapper.setter;

import java.util.function.BiConsumer;

import es.utils.mapper.factory.SetterFactory;

/**
 * This class customize the abstract class {@code Setter} creating a {@code Setter} instance from a {@code BiConsumer} 
 * @author eschoysman
 *
 * @param <U> the type of the destination object
 * @param <TMP> the type of the input of the {@code setter} operation
 * 
 * @see Setter
 * @see SetterFactory
 */
public class FunctionSetter<U,TMP> extends Setter<U,TMP> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param setter the {@code setter} operation
	 */
	public FunctionSetter(String name, BiConsumer<U,TMP> setter) {
		super(name,setter);
	}

}
