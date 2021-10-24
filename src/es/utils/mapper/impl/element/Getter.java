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

	private static final Getter<?,?> EMPTY = new Getter<>("getter_id_0",obj->null);

	private static int id_incr = 0;
	private int id = ++id_incr;

	private String name;
	private Function<T,GETTER_OUT> getter;

	/**
	 * Create a {@code Getter} instance with a identifier and a operation
	 * @param getter the {@code getter} operation
	 */
	public Getter(Function<T,GETTER_OUT> getter) {
		setName("getter_id_"+id);
		setGetter(getter);
	}
	/**
	 * Create a {@code Getter} instance with a identifier and a operation
	 * @param name the name identifier of the current {@code getter}
	 * @param getter the {@code getter} operation
	 */
	public Getter(String name, Function<T,GETTER_OUT> getter) {
		setName(name);
		setGetter(getter);
	}
	
	/**
	 * Returns an empty {@code getter} with no name and returns {@code null} value
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
	 * The unique id identifier of the current {@code getter}
	 * @return the unique id identifier of the current {@code getter}
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * The name identifier of the current {@code getter}
	 * @return the name identifier of the current {@code getter}
	 */
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = Objects.requireNonNull(name, "The name of the getter cannot be null");
	}
	private void setGetter(Function<T,GETTER_OUT> getter) {
		this.getter = Objects.requireNonNull(getter,"The getter operation cannot be null");
	}

	/**
	 * The value associated to the current {@code getter} during its creation
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
		return "Getter[id="+id+",name="+getName()+"]";
	}
	
}
