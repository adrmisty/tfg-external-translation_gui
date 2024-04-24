package main.java.util.exception;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Exception that arises due to issues when saving or reviewing the resulting
 * files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class ResultsException extends IOException {

    private static final long serialVersionUID = 1L;

    private boolean isReview;
    private ResourceBundle messages;

    public ResultsException() {
	super();
    }

    public ResultsException(boolean isReview) {
	this();
	this.isReview = isReview;
    }

    public ResultsException(ResourceBundle messages, boolean isReview) {
	this(isReview);
	this.messages = messages;
    }

    public boolean isManual() {
	return isReview;
    }

    @Override
    public String getLocalizedMessage() {
	if (isReview) {
	    return messages.getString("error.review");
	} else {
	    return messages.getString("error.save");
	}
    }

}
