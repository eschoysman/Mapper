package es.utils.mapper.utils;

import java.util.function.Function;
import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T>, Serializable {
    default void accept(T t) {
        this.acceptThrows(t, RuntimeException::new);
    }
    default <E extends Exception> void acceptThrows(T t, Function<Throwable, E> function) throws E {
        try {
            this.acceptThrows(t);
        }
        catch (Exception ex) {
            throw function.apply(ex);
        }
    }
    void acceptThrows(T p0) throws Exception;
}
