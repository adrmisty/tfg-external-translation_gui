package main.java.logic.image;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

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

    /**
     * Execution of automatic image captioning.
     * 
     * @param images list of paths pointing to images to caption
     * @throws IOException
     */
    public Properties generate(List<String> imageUrls) throws IOException {
	return apiVision.caption(imageUrls);
    }

}