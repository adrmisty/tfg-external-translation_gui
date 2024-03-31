package main.java.util.exception;

/**
 * Exception that arises when .properties file do not comply with expected
 * format (i.e. not i18n, non-valid properties, empty...).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class PropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    public PropertiesException(String message) {
	super(message);
    }

    public PropertiesException(String message, String fileName) {
	super(message + ": [" + fileName + "]");
    }
}
