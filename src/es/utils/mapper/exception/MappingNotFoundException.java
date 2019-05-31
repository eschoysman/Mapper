package es.utils.mapper.exception;

/**
 * A exception class that is thrown when an error occurs when a mapping does not exist.
 * @author eschoysman
 *
 */
public class MappingNotFoundException extends Exception {

	private static final long serialVersionUID = -6937492491136199843L;

	/**
     * @param message
     */
    public MappingNotFoundException(String message) {
        super(message);
    }
    
}
