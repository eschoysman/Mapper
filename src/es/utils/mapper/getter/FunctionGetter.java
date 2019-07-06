package es.utils.mapper.getter;

import java.util.function.Function;

import es.utils.mapper.factory.Factory;

/**
 * This class customize the abstract class {@code Getter} creating a {@code Getter} instance from a {@code Function} 
 * @author eschoysman
 *
 * @param <T> the type of the origin object
 * @param <TMP> the type of the result of the {@code getter} operation
 * @see Getter
 * @see Factory
 */
public class FunctionGetter<T,TMP> extends Getter<T,TMP> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param getter the {@code getter} operation
	 */
	public FunctionGetter(String name, Function<T,TMP> getter) {
		super(name,getter);
	}

}
