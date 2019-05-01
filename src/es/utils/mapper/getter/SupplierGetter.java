package es.utils.mapper.getter;

import java.util.function.Supplier;

/**
 * 
 * @author Emmanuel
 *
 * @param <T>
 * @param <TMP>
 */
public class SupplierGetter<T,TMP> extends FunctionGetter<T,TMP> {

	/**
	 * 
	 * @param name
	 * @param supplier
	 */
	public SupplierGetter(String name, Supplier<TMP> supplier) {
		super(name,in->supplier.get());
	}

}
