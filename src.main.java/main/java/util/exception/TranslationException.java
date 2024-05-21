package main.java.util.exception;

import java.util.ResourceBundle;

/**
 * Exception that arises due to issues during the Translation process (more
 * specifically, during contact with the API for the LLM). For example, when the
 * provided translation file is empty.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class TranslationException extends Exception {

    private static final long serialVersionUID = 1L;

    private boolean isManual;
    private ResourceBundle messages;

    public TranslationException() {
	super();
    }

    public TranslationException(boolean isManual) {
	this();
	this.isManual = isManual;
    }

    public TranslationException(ResourceBundle messages, boolean isManual) {
	this(isManual);
	this.messages = messages;
    }

    public boolean isManual() {
	return isManual;
    }

    @Override
    public String getLocalizedMessage() {
	if (isManual) {
	    return messages.getString("error.manual");
	} else {
	    return messages.getString("error.automatic");
	}
    }

}
