package es.utils.mapper.getter;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;

/**
 * This class customize {@code FunctionGetter} creating a {@code Getter} instance from a {@code Field}
 * @author eschoysman
 *
 * @param <T> the type of the origin object
 * @param <GETTER_OUT> the type of the result of the {@code getter} operation
 * 
 * @see FunctionGetter
 */
public class FieldGetter<T,GETTER_OUT> extends FunctionGetter<T,GETTER_OUT> {

	/**
	 * @param name the name identifier of the current {@code getter}
	 * @param field the field from which we want to get the value
	 */
	public FieldGetter(String name, Field field) {
		super(name,createGetter(field));
	}

	private static <T,OUT> Function<T,OUT> createGetter(Field field) {
		Objects.requireNonNull(field);
		field.setAccessible(true);
		return obj -> {
			OUT result = null;
			try {
				@SuppressWarnings("unchecked")
				OUT out = (OUT)field.get(obj);
				result = out;
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			}
			return result;
		};
	}
	
}
