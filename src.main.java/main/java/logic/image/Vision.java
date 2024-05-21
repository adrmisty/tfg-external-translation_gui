package main.java.logic.image;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import main.java.logic.image.api.AzureApiVision;
import main.java.util.exception.ImageException;
import main.java.util.exception.ResourceException;

/**
 * Manages automatic image description with Azure Cognitive Services Computer
 * Vision API. This class allows to automatically generate captions for a set of
 * images.
 * 
 * @author Adriana R.F. (uo282798)
 * @version May 2024
 */
public class Vision {

    // Azure Computer Vision API
    private ApiVision apiVision;

    // Results
    private Properties results;

    public Vision() throws ResourceException {
	this.apiVision = new AzureApiVision();
    }

    public Vision(ApiVision apiVision) {
	this.apiVision = apiVision;
    }

    /**
     * Execution of automatic image captioning.
     * 
     * @param images list of paths pointing to images to caption
     * @throws IOException
     * @throws ImageException
     */
    public Properties captions() {
	if (this.results == null) {
	    this.results = apiVision.caption();
	}
	return this.results;
    }

    /**
     * Saves files to caption.
     * 
     * @return valid images that have been set
     * @param file array containing image files
     * @throws ImageException if provided file images are invalid
     */
    public List<File> setImages(File[] file) throws ImageException {
	return apiVision.setImages(file);
    }

    /**
     * @return all image files that could not be processed
     */
    public List<File> getUnprocessedImages() {
	return apiVision.getUnprocessedImages();
    }

    /**
     * Combines the file-setting and auto-description functionalities for API
     * testing.
     * 
     * @param file
     * @return caption results
     * @throws ImageException
     */
    public Properties captions(File[] file) throws ImageException {
	return apiVision.caption(file);
    }

}