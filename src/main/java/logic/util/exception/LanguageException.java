package main.java.logic.util.exception;

import java.util.Locale;

/**
 * Exception that arises when specified locale is not supported by application
 * (in terms of API access, speech algorithms...).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class LanguageException extends Exception {

    private static final long serialVersionUID = 1L;

    private Locale language;

    public LanguageException(Locale language) {
	super();
	this.language = language;
    }

    public String getLanguage() {
	return language.getDisplayLanguage();
    }
}
