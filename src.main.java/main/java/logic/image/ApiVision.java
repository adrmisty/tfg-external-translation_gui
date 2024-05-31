package main.java.logic.image;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import main.java.util.exception.ImageException;

/**
 * Interface for API access aimed at image description and the validation of
 * input images.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface ApiVision {

    /**
     * Automatic image description over a list of valid images.
     * 
     * @throws ImageException in case that: - provided image is incorrect (i.e.
     *                        invalid size) - cannot be read/found from disk -
     *                        connection to the API is interrupted
     * @throws IOException    in case of any other potential failure reading and
     *                        processing
     */
    public Properties describe(List<File> file) throws ImageException;

    /**
     * From a given set of file images, validate them according to format/size
     * and build a list of the valid images that the API will be able to
     * describe automatically.
     * 
     * @param files array of images selected for description
     * @return list of those images among the ones given as input, that will be
     *         possible to describe
     */
    public List<File> validateImages(File[] files) throws ImageException;

    /**
     * Retrieves all valid files that can be processed by the API among the ones
     * previously given as input by the user.
     * 
     * @return list of valid image files
     */
    public List<File> getValidImages();

    /**
     * Retrieves all invalid files that cannot be processed by the API (due to
     * insufficient size or format), among the ones previously given as input by
     * the user.
     * 
     * @return list of invalid image files
     */
    public List<File> getInvalidImages();
}
