package main.java.logic.util.exception;

/**
 * Exception that arises when .properties file do not comply with expected
 * format: - invalid file name format (i.e. extension) - invalid content
 * (format, information, etc)
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class PropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private boolean contentRelated;

    public PropertiesException(String fileName) {
	super();
	this.fileName = fileName;
    }

    public PropertiesException(String fileName, boolean contentRelated) {
	super();
	this.fileName = fileName;
	this.contentRelated = contentRelated;
    }

    public boolean isContentRelated() {
	return contentRelated;
    }

    public String getFilename() {
	return fileName;
    }
}
