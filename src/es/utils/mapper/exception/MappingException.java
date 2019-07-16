package es.utils.mapper.exception;

/**
 * A exception class that is thrown when an error occurs during the mapping operation.
 * The thrown exception will contains a message explaining the cause of the error. 
 * @author eschoysman
 *
 */
public class MappingException extends Exception {

	private static final long serialVersionUID = 2496138712434409853L;

	/**
     * @param message the message of the exception
     */
    public MappingException(String message) {
    	super(message);
    }
    /**
     * @param message the message of the exception
     * @param cause the cause of the exception
     */
    public MappingException(String message, Throwable cause) {
    	super(message,cause);
    }

}
