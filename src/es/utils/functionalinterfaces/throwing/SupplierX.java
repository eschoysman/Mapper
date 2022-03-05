package es.utils.functionalinterfaces.throwing;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
/**
 * A sub-class of {@link Supplier} that handle the exceptions.
 * @author eschoysman
 * @param <T> type of the output argument
 */
public interface SupplierX<T> extends Supplier<T>, Serializable {
    /**
     * Try to call the {@code #getThrows()} method. If any error occurs, throws a {@code RuntimeException}.
     * @return the result of the supplier
     */
    default T get() {
        return this.getThrows(RuntimeException::new);
    }
    /**
     * Try to call the {@code #getThrows()} method. If any error occurs, throws the exception returned by the provided {@code exception}.
     * @param exception a function that takes a Throwable as input and returns an Exception to throw.
     * @param <E> the type of Exception to throw
     * @return the result of the supplier
     * @throws E if the body of this function throws an exception
     */
    default <E extends Exception> T getThrows(Function<Throwable,E> exception) throws E {
        try {
            return this.getThrows();
        }
        catch (Exception ex) {
            throw exception.apply(ex);
        }
    }
    /**
     * The body of the {@code SUpplierX} instance
     * @return the result of the supplier
     * @throws Exception allow the implementation to throw any kind of exception
     */
    T getThrows() throws Exception;
}
