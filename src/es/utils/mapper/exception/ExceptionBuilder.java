package es.utils.mapper.exception;

public class ExceptionBuilder<E extends Exception> {

    private Class<E> type;
    private String message;
    private Throwable cause;

    private ExceptionBuilder(Class<E> exceptionType) {
         this.type = exceptionType;
    }

    public static <E extends Exception> ExceptionBuilder<E> forType(Class<E> exceptionType) {
        return new ExceptionBuilder(exceptionType);
    }

    public ExceptionBuilder<E> message(String message) {
        this.message = message;
        return this;
    }

    public ExceptionBuilder<E> cause(Throwable cause) {
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
            switch(mask) {
                case 0: return buildEmpty();
                case 1: return buildCause();
                case 2: return buildMessage();
                case 3: return buildMessageCause();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("This should not append");
    }

    private E buildEmpty() throws Exception {
        return type.getDeclaredConstructor().newInstance();
    }
    private E buildMessage() throws Exception {
        return type.getDeclaredConstructor(String.class).newInstance(message);
    }
    private E buildCause() throws Exception {
        return type.getDeclaredConstructor(Throwable.class).newInstance(cause);
    }
    private E buildMessageCause() throws Exception {
        return type.getDeclaredConstructor(String.class, Throwable.class).newInstance(message,cause);
    }

}
