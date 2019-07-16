package es.utils.mapper.setter;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * This abstract class handle the logic of a generic {@code setter} operation.<br>
 * @author eschoysman
 *
 * @param <U> the type of the destination object
 * @param <SETTER_IN> the type of the input of the {@code setter} operation
 * 
 * @see FunctionSetter
 * @see FieldSetter
 */
public abstract class Setter<U,SETTER_IN> {

	private String name;
	private BiConsumer<U,SETTER_IN> setter;

	/**
	 * @param name the name identifier of the current {@code setter}
	 * @param setter the {@code setter} operation
	 */
	protected Setter(String name, BiConsumer<U,SETTER_IN> setter) {
		this.name = Objects.requireNonNull(name);
		this.setter = Objects.requireNonNull(setter);
	}
	
	/**
	 * @return the name identifier of the current {@code setter}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Apply the current {@code setter} operation to the destination object
	 * @param dest the destination object
	 * @param data the value to set in the destination object
	 */
	public void apply(U dest, SETTER_IN data) {
		setter.accept(dest,data);
	}
	
}
