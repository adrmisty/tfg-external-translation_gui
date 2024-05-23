package main.java.util.exception;

import java.util.ResourceBundle;

/**
 * Exception that arises due to issues when the results retrieved are not
 * complete.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class IncompleteResultsException extends ResultsException {

    private static final long serialVersionUID = 1L;

    public IncompleteResultsException() {
	super();
    }

    public IncompleteResultsException(ResourceBundle messages) {
	super(messages);
    }

    @Override
    public String getLocalizedMessage() {
	return messages.getString("error.incomplete");

    }

}
