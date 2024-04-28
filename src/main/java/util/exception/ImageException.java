package main.java.util.exception;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Exception that arises when an image file provided by the user is not valid: -
 * invalid size - unreadable - not found
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class ImageException extends Exception {

    private static final long serialVersionUID = 1L;

    private ResourceBundle messages;
    private List<File> files = new ArrayList<>();

    public ImageException() {
	super();
    }

    public ImageException(List<File> files) {
	super();
	this.files = files;
    }

    public ImageException(ResourceBundle messages, List<File> files) {
	this(files);
	this.messages = messages;
    }

    @Override
    public String getLocalizedMessage() {
	if (this.files.isEmpty()) {
	    return messages.getString("error.image");
	} else {
	    List<String> names = new ArrayList<>();
	    for (File f : files) {
		names.add(f.getName());
	    }
	    return String.format("%s\n[%s]", messages.getString("error.image2"),
		    String.join(" - ", names));
	}
    }

    public List<File> getInvalidFiles() {
	return files;
    }

}
