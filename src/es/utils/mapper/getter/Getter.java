package es.utils.mapper.getter;

import java.util.Objects;
import java.util.function.Function;

/**
 * 
 * @author Emmanuel
 *
 * @param <T>
 * @param <TMP>
 */
public abstract class Getter<T,TMP> {

	private static final Getter<?,?> EMPTY = new FunctionGetter<>("",Function.identity());
	
	private String name;
	private Function<T,TMP> getter;
	
	/**
	 * 
	 * @param name
	 * @param getter
	 */
	protected Getter(String name, Function<T,TMP> getter) {
		this.name = Objects.requireNonNull(name);
		this.getter = Objects.requireNonNull(getter);
	}
	
	/**
	 * 
	 * @return
	 */
	public static <T,TMP> Getter<T,TMP> empty() {
		@SuppressWarnings("unchecked")
		Getter<T,TMP> empty = (Getter<T,TMP>)EMPTY;
		return empty;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public TMP apply(T input) {
		return getter.apply(input);
	}
	
}
