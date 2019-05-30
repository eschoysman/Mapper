package es.utils.mapper.exception;

/**
 * A exception class that is thrown when an error occurs when a mapping does not exist.
 * @author eschyosman
 *
 */
public class MappingNotFoundException extends Exception {

	private static final long serialVersionUID = -6937492491136199843L;

	/**
     * 
     * @param s
     */
    public MappingNotFoundException(String s) {
        super(s);
    }
    
}
