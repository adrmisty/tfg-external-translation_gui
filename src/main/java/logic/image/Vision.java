package main.java.logic.image;

import java.io.File;
import java.io.IOException;
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
 * @version April 2024
 */
public class Vision {

    // Azure Computer Vision API
    private ApiVision apiVision;

    // Results
    private Properties results;

    public Vision() throws ResourceException {
	this.apiVision = new AzureApiVision();
    }

    /**
     * Execution of automatic image captioning.
     * 
     * @param images list of paths pointing to images to caption
     * @throws IOException
     * @throws ImageException
     */
    public Properties captions() throws ImageException {
	if (this.results == null) {
	    this.results = apiVision.caption();
	}
	return this.results;
    }

    /**
     * Saves files to caption.
     * 
     * @param file array containing image files
     */
    public void setImages(File[] file) {
	apiVision.setImages(file);
    }

}