package main.java.util.exception;

import java.util.ResourceBundle;

/**
 * Exception that arises when cognitive speech speaking over a text is
 * interrupted.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class SpeechException extends Exception {

    private static final long serialVersionUID = 1L;

    private ResourceBundle messages;

    public SpeechException() {
	super();
    }

    public SpeechException(ResourceBundle messages) {
	this();
	this.messages = messages;
    }

    @Override
    public String getLocalizedMessage() {
	return messages.getString("error.speech");
    }

}
