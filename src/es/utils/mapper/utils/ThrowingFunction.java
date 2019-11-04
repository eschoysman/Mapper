package es.utils.mapper.utils;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T,U> extends Function<T,U>, Serializable {

	@Override
	default U apply(T elem) {
		return applyThrows(elem, RuntimeException::new);
	}
	default <E extends Exception> U applyThrows(T elem, Function<Throwable,E> exception) throws E {
		try {
			return applyThrows(elem);
		} catch (Exception e) {
			throw exception.apply(e);
		}
	}
	U applyThrows(T elem) throws Exception;

}
