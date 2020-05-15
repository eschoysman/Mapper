package es.utils.mapper.exception;

import java.text.MessageFormat;

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
     * @param params the parameters object(s) to format
     * @return the current {@code CustomException} instance
     */
    public CustomException<E> message(CharSequence message, Object... params) {
        this.message = MessageFormat.format(message.toString(),params);
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
        try {
            switch (mask) {
                case 0:
                    return buildEmpty();
                case 1:
                    return buildCause();
                case 2:
                    return buildMessage();
                case 3:
                    return buildMessageCause();
            }
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("This should not append");
    }

    private E buildEmpty() throws Exception {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch(NoSuchMethodException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a empty constructor",e);
        }
    }
    private E buildMessage() throws Exception {
        try {
            return type.getDeclaredConstructor(String.class).newInstance(message);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a constructor taking only a String instance",e);
        }
    }
    private E buildCause() throws Exception {
        try {
            return type.getDeclaredConstructor(Throwable.class).newInstance(cause);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a constructor taking only a Throwable instance",e);
        }
    }
    private E buildMessageCause() throws Exception {
        try {
            return type.getDeclaredConstructor(String.class, Throwable.class).newInstance(message,cause);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException("Custom Exception "+type+" does not have a constructor taking both String and Throwable instances",e);
        }
    }

}
