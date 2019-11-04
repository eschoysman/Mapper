package es.utils.mapper.impl.element;

import java.util.Objects;
import java.util.function.Function;

/**
 * This class handle the logic of a generic {@code getter} operation.<br>
 * @author eschoysman
 * @param <T> the type of the origin object
 * @param <GETTER_OUT> the type of the result of the {@code getter} operation
 */
public class Getter<T,GETTER_OUT> {

	private static final Getter<?,?> EMPTY = new Getter<>("",obj->null);
	
	private String name;
	private Function<T,GETTER_OUT> getter;
	
	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param getter the {@code getter} operation
	 */
	public Getter(String name, Function<T,GETTER_OUT> getter) {
		this.name = Objects.requireNonNull(name);
		this.getter = Objects.requireNonNull(getter);
	}
	
	/**
	 * @return an empty {@code getter} with no name and returns {@code null} value
     * @param <T> the type of the origin object
     * @param <GETTER_OUT> the type of the result of the {@code getter} operation
	 */
	public static <T,GETTER_OUT> Getter<T,GETTER_OUT> empty() {
		@SuppressWarnings("unchecked")
		Getter<T,GETTER_OUT> empty = (Getter<T,GETTER_OUT>)EMPTY;
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
	public GETTER_OUT apply(T input) {
		return getter.apply(input);
	}
	
	/**
	 * Returns a human readable string of the current {@code Getter}
	 */
	@Override
	public String toString() {
		return "Getter[name="+getName()+"]";
	}
	
}
