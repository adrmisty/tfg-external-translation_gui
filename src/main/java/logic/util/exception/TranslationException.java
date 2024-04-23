package main.java.logic.util.exception;

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

    public TranslationException() {
	super();
    }

}
