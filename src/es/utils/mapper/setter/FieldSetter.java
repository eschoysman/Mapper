package es.utils.mapper.setter;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.BiConsumer;

public class FieldSetter<U,TMP> extends FunctionSetter<U,TMP> {

	public FieldSetter(Field field) {
		this(field.getName(),field);
	}
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
