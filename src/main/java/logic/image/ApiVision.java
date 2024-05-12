package main.java.logic.image;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import main.java.util.exception.ImageException;

/**
 * Interface for API access aimed at image description.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface ApiVision {

    /**
     * Automatic image description over a set of images, which have been set and
     * validated beforehand.
     * 
     * @throws IOException    in case of failure reading the image paths
     * @throws ImageException in case that: - provided image is incorrect (i.e.
     *                        invalid size) - cannot be read/found from disk
     */
    public Properties caption();

    /**
     * Automatic image description over a set of images, which are set and
     * validated in this same workflow.
     * 
     * @throws IOException    in case of failure reading the image paths
     * @throws ImageException in case that: - provided image is incorrect (i.e.
     *                        invalid size) - cannot be read/found from disk
     */
    public Properties caption(File[] file) throws ImageException;

    /**
     * Sets the images to caption.
     * 
     * @param files array of selected images
     */
    public List<File> setImages(File[] files) throws ImageException;

    /**
     * Retrieves all invalid files that could not be processed by the API (due
     * to insufficient size or format).
     * 
     * @return list of invalid image files
     */
    public List<File> getUnprocessedImages();
}
