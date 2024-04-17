package main.java.logic.image;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

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
     * @throws IOException in case of failure reading the image paths
     */
    public Properties caption() throws IOException;

    /**
     * Sets the images to caption.
     * 
     * @param files array of selected images
     */
    public void setImages(File[] files);
}
