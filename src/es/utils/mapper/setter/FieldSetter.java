package es.utils.mapper.setter;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * This class customize {@code FunctionGetter} creating a {@code Getter} instance from a {@code Field}
 * @author eschoysman
 *
 * @param <U> the type of the destination object
 * @param <SETTER_IN> the type of the input of the {@code setter} operation
 * 
 * @see FunctionSetter
 */
public class FieldSetter<U,SETTER_IN> extends FunctionSetter<U,SETTER_IN> {

	/**
	 * 
	 * @param name the name identifier of this {@code setter}
	 * @param field the field used to set the value
	 */
	public FieldSetter(String name, Field field) {
		super(name,createSetter(field));
	}

	private static <T,IN> BiConsumer<T,IN> createSetter(Field field) {
		Objects.requireNonNull(field);
		field.setAccessible(true);
		return (obj,data) -> {
			try {
				field.set(obj,data);
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			}
		};
	}
	
}
