package es.utils.mapper.getter;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;

public class FieldGetter<T,TMP> extends FunctionGetter<T,TMP> {

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
