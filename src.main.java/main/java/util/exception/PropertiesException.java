package main.java.util.exception;

import java.util.ResourceBundle;

/**
 * Exception that arises when .properties file (found in /resources or not) do
 * not comply with expected format: - invalid file name format (i.e. extension)
 * - invalid content (format, information, etc)
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class PropertiesException extends ResourceException {

    private static final long serialVersionUID = 1L;

    private ResourceBundle messages;
    private String fileName;
    private boolean contentRelated;

    public PropertiesException(String fileName) {
	super(fileName);
	this.fileName = fileName;
    }

    public PropertiesException(String fileName, boolean contentRelated) {
	super(fileName);
	this.fileName = fileName;
	this.contentRelated = contentRelated;
    }

    public PropertiesException(ResourceBundle messages, String fileName,
	    boolean contentRelated) {
	this(fileName, contentRelated);
	this.messages = messages;
    }

    @Override
    public String getLocalizedMessage() {
	if (this.isContentRelated()) {
	    return messages.getString("error.file") + this.fileName;
	} else {
	    return messages.getString("error.property.fileName")
		    + this.fileName;
	}
    }

    public boolean isContentRelated() {
	return contentRelated;
    }

    public String getFilename() {
	return fileName;
    }
}
