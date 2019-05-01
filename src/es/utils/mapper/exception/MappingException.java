package es.utils.mapper.exception;

/**
 * 
 * @author Emmanuel
 *
 */
public class MappingException extends Exception {

	private static final long serialVersionUID = 2496138712434409853L;

	/**
     * 
     * @param message
     */
    public MappingException(String message) {
    	super(message);
    }
    /**
     * 
     * @param message
     * @param cause
     */
    public MappingException(String message, Throwable cause) {
    	super(message,cause);
    }

}
