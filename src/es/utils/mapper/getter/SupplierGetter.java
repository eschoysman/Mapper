package es.utils.mapper.getter;

import java.util.function.Supplier;

import es.utils.mapper.factory.Factory;

/**
 * This class customize {@code FunctionGetter} creating a {@code Getter} instance from a {@code Supplier}
 * @author eschoysman
 * @param <T> the type of the origin object
 * @param <TMP> the type of the result of the {@code getter} operation
 * 
 * @see FunctionGetter
 * @see Factory
 */
public class SupplierGetter<T,TMP> extends FunctionGetter<T,TMP> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param supplier the supplier of the value
	 */
	public SupplierGetter(String name, Supplier<TMP> supplier) {
		super(name,in->supplier.get());
	}

}
