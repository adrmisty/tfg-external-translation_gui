package main.java.logic.image;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import main.java.logic.util.exception.ImageException;

/**
 * Interface for API access aimed at image description.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface ApiVision {

    /**
     * Automatic image description over a set of images.
     * 
     * @throws IOException    in case of failure reading the image paths
     * @throws ImageException in case that: - provided image is incorrect (i.e.
     *                        invalid size) - cannot be read/found from disk
     */
    public Properties caption() throws ImageException;

    /**
     * Sets the images to caption.
     * 
     * @param files array of selected images
     */
    public void setImages(File[] files);
}
