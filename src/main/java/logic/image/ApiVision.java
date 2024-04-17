package main.java.logic.image;

import java.io.IOException;
import java.util.List;
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
     * @param paths list of paths pointing to images to caption
     * @throws IOException in case of failure reading the image paths
     */
    public Properties caption(List<String> paths) throws IOException;
}
