package es.utils.mapper.getter;

import es.utils.mapper.factory.Factory;

/**
 * This class customize {@code SupplierGetter} creating a {@code Getter} instance from a constant value
 * @author eschoysman
 * @param <T> the type of the origin object
 * @param <GETTER_OUT> the type of the result of the {@code getter} operation
 * 
 * @see SupplierGetter
 * @see Factory
 */
public class ValueGetter<T,GETTER_OUT> extends SupplierGetter<T,GETTER_OUT> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param value the constant value
	 */
	public ValueGetter(String name, GETTER_OUT value) {
		super(name,()->value);
	}

}
