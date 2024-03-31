package main.java.util.exception;

/**
 * Exception that arises when requested resource cannot be loaded properly.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class ResourceException extends Exception {

    private static final long serialVersionUID = 1L;

    public ResourceException(String message, String resourceName) {
	super(message + ": [" + resourceName + "]");
    }

    public ResourceException(String message) {
	super(message);
    }

}
