package es.utils.mapper.exception;

public class MappingException extends Exception {
    private static final long serialVersionUID = 1L;

    public MappingException(String message) {
    	super(message);
    }
    public MappingException(String message, Throwable cause) {
    	super(message,cause);
    }

}
