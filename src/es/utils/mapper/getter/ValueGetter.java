package es.utils.mapper.getter;

import es.utils.mapper.factory.Factory;

/**
 * This class customize {@code SupplierGetter} creating a {@code Getter} instance from a constant value
 * @author eschoysman
 * @param <T> the type of the origin object
 * @param <TMP> the type of the result of the {@code getter} operation
 * 
 * @see SupplierGetter
 * @see Factory
 */
public class ValueGetter<T,TMP> extends SupplierGetter<T,TMP> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param value the constant value
	 */
	public ValueGetter(String name, TMP value) {
		super(name,()->value);
	}

}
