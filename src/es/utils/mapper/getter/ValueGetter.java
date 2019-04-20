package es.utils.mapper.getter;

public class ValueGetter<T,TMP> extends SupplierGetter<T,TMP> {

	public ValueGetter(String name, TMP value) {
		super(name,()->value);
	}

}
