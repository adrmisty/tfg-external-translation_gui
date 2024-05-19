package main.java.util.exception;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Exception that arises when the user does not have a default application for
 * editing files of a given format in their sytem.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class IdeException extends IOException {

    private static final long serialVersionUID = 1L;

    private ResourceBundle messages;
    private boolean isReview;

    public IdeException() {
	super();
    }

    public IdeException(ResourceBundle messages) {
	this();
	this.messages = messages;
    }

    public IdeException(ResourceBundle messages, boolean isReview) {
	this(messages);
	this.isReview = isReview;
    }

    @Override
    public String getLocalizedMessage() {
	if (!isReview) {
	    return messages.getString("error.ide");
	}
	return messages.getString("error.ide.review");
    }

}
