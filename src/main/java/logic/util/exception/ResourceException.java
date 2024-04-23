package main.java.logic.util.exception;

/**
 * Exception that arises when requested resource cannot be loaded properly.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class ResourceException extends Exception {

    private static final long serialVersionUID = 1L;

    public String resourceName;

    public ResourceException(String resourceName) {
	super();
    }

    public String getResourceName() {
	return resourceName;
    }

}
