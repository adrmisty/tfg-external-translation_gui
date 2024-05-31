package main.java.logic.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import main.java.logic.image.api.AzureApiVision;
import main.java.util.exception.ImageException;
import main.java.util.exception.ResourceException;

/**
 * Manages the automatic description of a set of images by accessing a computer
 * vision/image description API.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class Vision {

    // Computer vision/Image description API
    private ApiVision apiVision;

    // Images to caption and its results
    private List<File> images = new ArrayList<>();
    private Properties results;

    public Vision() throws ResourceException {
	this.apiVision = new AzureApiVision();
    }

    public Vision(ApiVision apiVision) {
	this.apiVision = apiVision;
    }

    /**
     * Describes the specified set of images, automatically, through a computer
     * vision/image recognition API.
     * 
     * @throws IOException    in case of issues reading the file images
     * @throws ImageException in case of inability to complete image description
     *                        due to invalid format/size of the image(s) or
     *                        interruption of the connection to the API
     */
    public Properties describe() throws ImageException {
	if (this.results == null) {
	    this.results = apiVision.describe(this.images);
	}
	return this.results;
    }

    /**
     * Saves the specified files to be described, after they have been
     * validated.
     * 
     * @return valid images that have been set, as a list
     * @param imageFiles file array containing image files
     * @throws ImageException if provided file images are invalid
     */
    public List<File> setImages(File[] imageFiles) throws ImageException {
	try {
	    this.images = apiVision.validateImages(imageFiles);
	    return this.images;
	} catch (Exception e) {
	    this.images = apiVision.getValidImages();
	    throw e;
	}
    }

    /**
     * @return all image files that can not be processed by the API due to
     *         incorrect format or size, among the ones previously given as
     *         input
     */
    public List<File> getUnprocessedImages() {
	return apiVision.getInvalidImages();
    }

    /**
     * Resets all computer vision stored data (valid images, invalid images and
     * results).
     */
    public void reset() {
	this.images = new ArrayList<>();
	this.results = null;
	this.apiVision = new AzureApiVision();
    }
}