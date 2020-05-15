package es.utils.mapper.utils;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A sub-class of {@link Consumer} that handle the exceptions.
 * @author eschoysman
 * @param <T> type of the element to consume
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T>, Serializable {
	/**
	 * Try to call the {@code acceptThrows(elem)} method. If any error occurs, throws a {@code RuntimeException}.
	 */
    default void accept(T elem) {
        this.accept(elem, RuntimeException::new);
    }
    /**
     * Try to call the {@code #acceptThrows(Object)} method. If any error occurs, throws the exception returned by the provided {@code function}.
     * @param elem the element to consume
     * @param function a function that takes a Throwable as input and returns an Exception to throw.
     * @param <E> the type of Exception to throw
     * @throws E if the body of this function throws an exception
     */
    default <E extends Exception> void accept(T elem, Function<Throwable, E> function) throws E {
        try {
            this.acceptThrows(elem);
        }
        catch (Exception ex) {
            throw function.apply(ex);
        }
    }
    /**
     * The body of the {@code ThrowingConsumer} instance
     * @param elem the object to be consumed
     * @throws Exception allow the implementation to throw any kind of exception
     */
    void acceptThrows(T elem) throws Exception;
}
