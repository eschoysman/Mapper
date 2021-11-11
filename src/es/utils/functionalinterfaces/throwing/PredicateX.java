package es.utils.functionalinterfaces.throwing;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
/**
 * A sub-class of {@link Predicate} that handle the exceptions.
 * @author eschoysman
 * @param <T> type of the input argument
 */
public interface PredicateX<T> extends Predicate<T>, Serializable {
    /**
     * Try to call the {@code #testThrows(Object)} method. If any error occurs, throws a {@code RuntimeException}.
     * @return the result of the predicate
     */
    default boolean test(T elem) {
        return this.testThrows(elem, RuntimeException::new);
    }
    /**
     * Try to call the {@code #testThrows(Object)} method. If any error occurs, throws the exception returned by the provided {@code exception}.
     * @param elem the element to test
     * @param exception a function that takes a Throwable as input and returns an Exception to throw.
     * @param <E> the type of Exception to throw
     * @return the result of the predicate
     * @throws E if the body of this function throws an exception
     */
    default <E extends Exception> boolean testThrows(T elem, Function<Throwable,E> exception) throws E {
        try {
            return this.testThrows(elem);
        }
        catch (Exception ex) {
            throw exception.apply(ex);
        }
    }
    /**
     * The body of the {@code PredicateX} instance
     * @param elem the object to test
     * @return the result of the predicate
     * @throws Exception allow the implementation to throw any kind of exception
     */
    boolean testThrows( T elem) throws Exception;
}
