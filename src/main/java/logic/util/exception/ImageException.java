package main.java.logic.util.exception;

import java.io.File;
import java.util.List;

/**
 * Exception that arises when an image file provided by the user is not valid: -
 * invalid size - unreadable - not found
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class ImageException extends Exception {

    private static final long serialVersionUID = 1L;
    private int width;
    private int height;
    private List<File> files;

    public ImageException() {
	super();
    }

    public ImageException(Exception e) {
	super(e);
    }

    public ImageException(int width, int height) {
	super();
	this.width = width;
	this.height = height;
    }

    public ImageException(List<File> files) {
	super();
	this.files = files;
    }

    public List<File> getInvalidFiles() {
	return files;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

}
