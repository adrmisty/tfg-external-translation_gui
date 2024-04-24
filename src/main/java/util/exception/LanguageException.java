package main.java.util.exception;

import java.util.Locale;
import java.util.ResourceBundle;

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
    private String strLanguage;
    private ResourceBundle messages;

    public LanguageException(Locale language) {
	super();
	this.language = language;
    }

    public LanguageException(ResourceBundle messages, String language) {
	super();
	this.messages = messages;
	this.strLanguage = language;
    }

    public LanguageException(ResourceBundle messages, Locale language) {
	this(language);
	this.messages = messages;
    }

    @Override
    public String getLocalizedMessage() {
	return messages.getString("error.localization") + " " + getLanguage();
    }

    public String getLanguage() {
	if (language == null) {
	    return strLanguage;
	}
	return language.getDisplayLanguage();
    }

}
