package es.utils.mapper.setter;

import java.util.function.BiConsumer;

import es.utils.mapper.factory.Factory;

/**
 * This class customize the abstract class {@code Setter} creating a {@code Setter} instance from a {@code BiConsumer} 
 * @author eschoysman
 *
 * @param <U> the type of the destination object
 * @param <SETTER_IN> the type of the input of the {@code setter} operation
 * 
 * @see Setter
 * @see Factory
 */
public class FunctionSetter<U,SETTER_IN> extends Setter<U,SETTER_IN> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param setter the {@code setter} operation
	 */
	public FunctionSetter(String name, BiConsumer<U,SETTER_IN> setter) {
		super(name,setter);
	}

}
