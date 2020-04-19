package exception;

public class ExampleException extends Exception {

    public ExampleException() {
    }

    public ExampleException(String message) {
        super(message);
    }

    public ExampleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExampleException(Throwable cause) {
        super(cause);
    }

}
