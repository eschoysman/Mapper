package es.utils.mapper.getter;

import java.util.function.Supplier;

public class SupplierGetter<T,TMP> extends FunctionGetter<T,TMP> {

	public SupplierGetter(String name, Supplier<TMP> supplier) {
		super(name,in->supplier.get());
	}

}
