package main.java.util.exception;

/**
 * Exception that arises due to issues during the Translation process (more
 * specifically, during contact with the API for the LLM).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class TranslationException extends Exception {

    private static final long serialVersionUID = 1L;

    public TranslationException(String message) {
	super(message);
    }

}
