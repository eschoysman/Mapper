package es.utils.mapper.getter;

import java.util.Objects;
import java.util.function.Function;

public abstract class Getter<T,TMP> {

	private static final Getter<?,?> EMPTY = new FunctionGetter<>("",Function.identity());
	
	private String name;
	private Function<T,TMP> getter;
	
	protected Getter(String name, Function<T,TMP> getter) {
		this.name = Objects.requireNonNull(name);
		this.getter = Objects.requireNonNull(getter);
	}
	
	public static <T,TMP> Getter<T,TMP> empty() {
		@SuppressWarnings("unchecked")
		Getter<T,TMP> empty = (Getter<T,TMP>)EMPTY;
		return empty;
	}
	
	public String getName() {
		return name;
	}

	public TMP apply(T input) {
		return getter.apply(input);
	}
	
}
