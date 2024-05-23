package main.java.util.exception;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Exception that arises due to issues when saving or reviewing the resulting
 * files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class ResultsException extends IOException {

    private static final long serialVersionUID = 1L;

    private boolean isReview = false;
    private boolean isFilePath = false;
    private String move = "";
    protected ResourceBundle messages;

    public ResultsException() {
	super();
    }

    public ResultsException(boolean isReview) {
	this();
	this.isReview = isReview;
    }

    public ResultsException(boolean isReview, boolean isFilePath, String move) {
	this(isReview);
	this.isFilePath = isFilePath;
	this.move = move;
    }

    public ResultsException(ResourceBundle messages) {
	this();
	this.messages = messages;
    }

    public ResultsException(ResourceBundle messages, boolean isReview) {
	this(isReview);
	this.messages = messages;
    }

    public ResultsException(ResourceBundle messages, boolean isReview,
	    boolean isFilePath, String move) {
	this(messages, isReview);
	this.isFilePath = isFilePath;
	this.move = move;
    }

    public boolean isReview() {
	return isReview;
    }

    public boolean isFilePath() {
	return isFilePath;
    }

    public String getFileMove() {
	return move;
    }

    @Override
    public String getLocalizedMessage() {
	if (isFilePath) {
	    return messages.getString("error.filepath") + move;
	}
	if (isReview) {
	    return messages.getString("error.review");
	} else {
	    return messages.getString("error.save");
	}
    }

}
