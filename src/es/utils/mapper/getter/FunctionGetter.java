package es.utils.mapper.getter;

import java.util.function.Function;

/**
 * This class customize the abstract class {@code Getter} creating a {@code Getter} instance from a {@code Function} 
 * @author eschoysman
 *
 * @param <T> the type of the origin object
 * @param <GETTER_OUT> the type of the result of the {@code getter} operation
 * @see Getter
 */
public class FunctionGetter<T,GETTER_OUT> extends Getter<T,GETTER_OUT> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param getter the {@code getter} operation
	 */
	public FunctionGetter(String name, Function<T,GETTER_OUT> getter) {
		super(name,getter);
	}

}
