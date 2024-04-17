package main.java.logic.image.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVision;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageDescription;

import main.java.logic.image.ApiVision;
import main.java.logic.util.exception.ResourceException;
import main.java.logic.util.properties.ResourceLoader;

/**
 * API access for image description functionality provided by Computer Vision
 * services.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class AzureApiVision implements ApiVision {

    // Computer vision abstraction
    private ComputerVision cv;

    public AzureApiVision() throws ResourceException {
	ComputerVisionClient azureClient = ComputerVisionManager
		.authenticate(ResourceLoader.getAzureVisionApiKey())
		.withEndpoint(ResourceLoader.getAzureVisionEndpoint());

	this.cv = azureClient.computerVision();
    }

    @Override
    public Properties caption(List<String> paths) throws IOException {
	Properties pr = new Properties();
	String caption;
	String path;

	for (int i = 0; i < paths.size(); i++) {
	    path = paths.get(i);
	    ImageDescription d = cv.describeImageInStream(getBytes(path), null);
	    caption = d.captions().get(0).text();

	    System.out.println(caption);
	    pr.put("image_" + i, caption);
	}

	return pr;
    }

    /**
     * @param path image path, as input
     * @return bytes object representing image data
     * @throws IOException in case file does not exist, or any other issue while
     *                     reading
     */
    private byte[] getBytes(String path) throws IOException {
	File imageFile = new File(path);
	FileInputStream inputStream = new FileInputStream(imageFile);

	try {
	    // Create a byte array to store the image data
	    long size = imageFile.length();
	    byte[] bytes = new byte[(int) size];

	    // Read the image data into the byte array
	    int read = inputStream.read(bytes);
	    if (read != size) {
		throw new IOException("Error reading image file");
	    }

	    return bytes;
	} finally {
	    // Close the input stream
	    inputStream.close();
	}
    }

}
