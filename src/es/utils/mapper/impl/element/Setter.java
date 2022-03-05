package es.utils.mapper.impl.element;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * This class handle the logic of a generic {@code setter} operation.<br>
 * @author eschoysman
 *
 * @param <U> the type of the destination object
 * @param <SETTER_IN> the type of the input of the {@code setter} operation
 */
public class Setter<U,SETTER_IN> {

	private static int id_incr = 0;
	private int id = ++id_incr;

	private String name;
	private BiConsumer<U,SETTER_IN> setter;

	private static final Setter<?,?> EMPTY = new Setter<>("setter_id_0",(obj,setter)->{});

	/**
	 * Create a {@code Ssetter} instance with a identifier and a operation
	 * @param setter the {@code setter} operation
	 */
	public Setter(BiConsumer<U,SETTER_IN> setter) {
		setName("setter_id_"+id);
		setSetter(setter);
	}
	/**
	 * Create a {@code Setter} instance with a identifier and a operation
	 * @param name the name identifier of the current {@code setter}
	 * @param setter the {@code setter} operation
	 */
	public Setter(String name, BiConsumer<U,SETTER_IN> setter) {
		setName(name);
		setSetter(setter);
	}
	
	/**
	 * Returns an empty {@code setter} with no name and empty operation
	 * @return an empty {@code setter} with no name and empty operation
     * @param <U> the type of the origin object
     * @param <SETTER_IN> the type of the result of the {@code setter} operation
	 */
	public static <U,SETTER_IN> Setter<U,SETTER_IN> empty() {
		@SuppressWarnings("unchecked")
		Setter<U,SETTER_IN> empty = (Setter<U,SETTER_IN>)EMPTY;
		return empty;
	}

	/**
	 * The unique id identifier of the current {@code setter}
	 * @return the unique id identifier of the current {@code setter}
	 */
	public int getId() {
		return id;
	}

	/**
	 * The name identifier of the current {@code setter}
	 * @return the name identifier of the current {@code setter}
	 */
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = Objects.requireNonNull(name,"The name of the setter cannot be null");
	}
	private void setSetter(BiConsumer<U,SETTER_IN> setter) {
		this.setter = Objects.requireNonNull(setter,"The setter operation cannot be null");
	}

	/**
	 * Apply the current {@code setter} operation to the destination object
	 * @param dest the destination object
	 * @param data the value to set in the destination object
	 */
	public void apply(U dest, SETTER_IN data) {
		setter.accept(dest,data);
	}
	
	/**
	 * Returns a human readable string of the current {@code Setter}
	 */
	@Override
	public String toString() {
		return "Setter[id="+id+",name="+getName()+"]";
	}

}
