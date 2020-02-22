package es.utils.mapper.utils;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
/**
 * A sub-class of {@link Function} that handle the exceptions.
 * @author eschoysman
 * @param <T> type of the element in input
 * @param <U> type of the element in output
 */
public interface ThrowingFunction<T,U> extends Function<T,U>, Serializable {
	/**
	 * Try to call the {@code #applyThrows(Object)} method. If any error occurs, throws a {@code RuntimeException}.
	 * @return the result of the function
	 */
	default U apply(T elem) {
		return apply(elem, RuntimeException::new);
	}
    /**
     * Try to call the {@code #applyThrows(Object)} method. If any error occurs, throws the exception returned by the provided {@code exception}.
     * @param elem the element to consume
     * @param exception a function that takes a Throwable as input and returns an Exception to throw.
     * @param <E> the type of Exception to throw
     * @return the result of the function
     * @throws E if the body of this function throws an exception
     */
	default <E extends Exception> U apply(T elem, Function<Throwable,E> exception) throws E {
		try {
			return applyThrows(elem);
		} catch (Exception e) {
			throw exception.apply(e);
		}
	}
    /**
     * The body of the {@code ThrowingFunction} instance
     * @param elem the object in input
     * @return the result of the function
     * @throws Exception allow the implementation to throw any kind of exception
     */
	U applyThrows(T elem) throws Exception;

}
