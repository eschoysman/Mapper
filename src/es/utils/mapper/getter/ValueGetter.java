package es.utils.mapper.getter;

/**
 * 
 * @author eschyosman
 *
 * @param <T>
 * @param <TMP>
 */
public class ValueGetter<T,TMP> extends SupplierGetter<T,TMP> {

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public ValueGetter(String name, TMP value) {
		super(name,()->value);
	}

}
