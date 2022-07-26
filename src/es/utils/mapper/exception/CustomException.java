package es.utils.mapper.exception;

import java.lang.reflect.InvocationTargetException;

public class CustomException<E extends Exception> {

    private Class<E> type;
    private String message;
    private Throwable cause;

    private CustomException(Class<E> exceptionType) {
         this.type = exceptionType;
    }

    /**
     * Create a {@code CustomException} builder for custom exception.
     * @param customException the type of the exception to throw
     * @param <E> the class type of the exception to throw
     * @return a {@code CustomException} instance that will be used to throw custom exception with optional message
     * and optional cause
     */
    public static <E extends Exception> CustomException<E> forType(Class<E> customException) {
        return new CustomException(customException);
    }

    /**
     * Set the message for the exception to throw. The {@code message} can be a pattern accepted by the
     * {@code MessageFormat} class, in that case the {@code params} are used.
     * @param message the string or format of the message
     * @return the current {@code CustomException} instance
     */
    public CustomException<E> message(CharSequence message) {
        this.message = message.toString();
        return this;
    }

    /**
     * Set the cause of the exception to throw
     * @param cause the cause of the exception
     * @return the current {@code CustomException} instance
     */
    public CustomException<E> cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    /**
     * Create the custom exception to throw or throw a {@code RuntimeException} if any error occurs
     * @return the custom exception to throw
     */
    public E build() {
        int mask = 0;
        if(message!=null) {
            mask += 2;
        }
        if(cause!=null) {
            mask += 1;
        }
        switch (mask) {
            case 1:
                return buildCause();
            case 2:
                return buildMessage();
            case 3:
                return buildMessageCause();
            case 0:
            default:
                return buildEmpty();
        }
    }

    private E buildEmpty() {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a empty constructor. Please provide a message and/or a cause error.",e);
        }
    }
    private E buildMessage() {
        try {
            return type.getDeclaredConstructor(String.class).newInstance(message);
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a constructor taking only a String instance. Please provide a cause error or don't pass any message.",e);
        }
    }
    private E buildCause() {
        try {
            return type.getDeclaredConstructor(Throwable.class).newInstance(cause);
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a constructor taking only a Throwable instance. Please provide a message error or don't pass any cause.",e);
        }
    }
    private E buildMessageCause() {
        try {
            return type.getDeclaredConstructor(String.class, Throwable.class).newInstance(message,cause);
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a constructor taking both String and Throwable instances. Please provide only a message ora cause error or none of them.",e);
        }
    }

}
