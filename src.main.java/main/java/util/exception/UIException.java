package main.java.util.exception;

import java.util.ResourceBundle;

/**
 * Unrecoverable exception due to problems with building of the application's
 * UI.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class UIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResourceBundle messages;

    public UIException() {
	super();
    }

    public UIException(ResourceBundle messages) {
	super();
	this.messages = messages;
    }

    @Override
    public String getLocalizedMessage() {
	return messages.getString("error.ui");
    }

}
