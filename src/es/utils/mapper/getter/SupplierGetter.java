package es.utils.mapper.getter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * This class customize {@code FunctionGetter} creating a {@code Getter} instance from a {@code Supplier}
 * @author eschoysman
 * @param <T> the type of the origin object
 * @param <GETTER_OUT> the type of the result of the {@code getter} operation
 * 
 * @see FunctionGetter
 */
public class SupplierGetter<T,GETTER_OUT> extends FunctionGetter<T,GETTER_OUT> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param supplier the supplier of the value
	 */
	public SupplierGetter(String name, Supplier<GETTER_OUT> supplier) {
		super(name,in->Objects.requireNonNull(supplier).get());
	}

}
