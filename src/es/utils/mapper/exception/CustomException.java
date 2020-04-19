package es.utils.mapper.exception;

public class CustomException<E extends Exception> {

    private Class<E> type;
    private String message;
    private Throwable cause;

    private CustomException(Class<E> exceptionType) {
         this.type = exceptionType;
    }

    public static <E extends Exception> CustomException<E> forType(Class<E> customException) {
        return new CustomException(customException);
    }

    public CustomException<E> message(String message) {
        this.message = message;
        return this;
    }

    public CustomException<E> cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public E build() {
        int mask = 0;
        if(message!=null) {
            mask+=2;
        }
        if(cause!=null) {
            mask+=1;
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
