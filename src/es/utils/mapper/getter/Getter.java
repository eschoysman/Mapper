package es.utils.mapper.getter;

import java.util.Objects;
import java.util.function.Function;

/**
 * This abstract class handle the logic of a generic {@code getter} operation.<br>
 * @author eschoysman
 *
 * @param <T> the type of the origin object
 * @param <TMP> the type of the result of the {@code getter} operation
 * 
 * @see FunctionGetter
 * @see FieldGetter
 * @see SupplierGetter
 * @see ValueGetter
 */
public abstract class Getter<T,TMP> {

	private static final Getter<?,?> EMPTY = new FunctionGetter<>("",obj->null);
	
	private String name;
	private Function<T,TMP> getter;
	
	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param getter the {@code getter} operation
	 */
	protected Getter(String name, Function<T,TMP> getter) {
		this.name = Objects.requireNonNull(name);
		this.getter = Objects.requireNonNull(getter);
	}
	
	/**
	 * @return an empty {@code getter} with no name and identity operation
     * @param <T> the type of the origin object
     * @param <TMP> the type of the result of the {@code getter} operation
	 */
	public static <T,TMP> Getter<T,TMP> empty() {
		@SuppressWarnings("unchecked")
		Getter<T,TMP> empty = (Getter<T,TMP>)EMPTY;
		return empty;
	}
	
	/**
	 * @return the name identifier of the current {@code getter}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param input the object on which the current {@code getter} is to be applied 
	 * @return the value associated to the current {@code getter} during its creation
	 */
	public TMP apply(T input) {
		return getter.apply(input);
	}
	
}
