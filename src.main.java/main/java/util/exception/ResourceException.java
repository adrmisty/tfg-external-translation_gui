package main.java.util.exception;

import java.util.ResourceBundle;

/**
 * Exception that arises when requested resource cannot be loaded properly.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class ResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String resourceName;
    public ResourceBundle messages;

    public ResourceException(String resourceName) {
	super();
	this.resourceName = resourceName;
    }

    public ResourceException(ResourceBundle messages, String resourceName) {
	this(resourceName);
	this.messages = messages;
    }

    @Override
    public String getLocalizedMessage() {
	if (messages == null) {
	    return "Resource exception -> " + getResourceName();
	}
	return messages.getString("error.resource") + getResourceName();
    }

    public String getResourceName() {
	return resourceName;
    }

}
