package es.utils.functionalinterfaces.throwing;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class can be use to simply catch the exception of a {@code Function} or a {@code Consumer} such that you does not
 * have to explicitly do a {@code try-catch} inside an {@code Stream} or {@code Optional} operation. It is useful for all
 * that cases you have to pass a {@code Function} or a {@code Consumer} but the called method throw some exception.<br>
 * As the {@code FunctionX} is a sub-class of {@code Function}, is possible to pass to the
 * {@code handleException(FunctionX)} method a Function ({@code handleException(Function)})
 * that normally want to explicitly catch an exception. The cast to @{@code FunctionX} is automatically done and
 * if an exception is thrown, it will be wrapped in a {@code RuntimeException}.<br><br>
 * Example:<br>
 * <ul>
 *     <li>Vanilla Java:<br>
 *<pre>myStream.peek($-&gt;try {
 *      Thread.sleep(100);
 *  } catch (InterruptedException e) {
 *      e.printStackTrace();
 *  });
 *</pre>
 *     </li>
 *     <li>using ExceptionHandle class:<br>
 *<pre>myStream.peek(handleConsumer($-&gt;Thread.sleep(100)));</pre>
 *     </li>
 * </ul><br>
 */
public class ExceptionHandle {

    public static <T,U> Function<T,U> handleFunction(FunctionX<T,U> function) {
        return function;
    }
    public static <T> Consumer<T> handleConsumer(ConsumerX<T> consumer) {
        return consumer;
    }

}
