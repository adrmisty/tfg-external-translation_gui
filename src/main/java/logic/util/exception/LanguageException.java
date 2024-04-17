package main.java.logic.util.exception;

import java.util.Locale;

/**
 * Exception that arises when specified locale is not supported by application
 * (in terms of API access, speech algorithms...).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class LanguageException extends Exception {

    private static final long serialVersionUID = 1L;

    public LanguageException(String message) {
	super(message);
    }

    public LanguageException(String message, Locale locale) {
	super(message + ": [" + locale.getDisplayLanguage() + "]");
    }
}
