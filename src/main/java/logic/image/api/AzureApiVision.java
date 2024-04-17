package main.java.logic.image.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    // Files
    private File[] files;

    public AzureApiVision() throws ResourceException {
	ComputerVisionClient azureClient = ComputerVisionManager
		.authenticate(ResourceLoader.getAzureVisionApiKey())
		.withEndpoint(ResourceLoader.getAzureVisionEndpoint());

	this.cv = azureClient.computerVision();
    }

    @Override
    public void setImages(File[] file) {
	if (file != null) {
	    this.files = file;
	}
    }

    @Override
    public Properties caption() throws IOException {
	if (this.files == null) {
	    return null;
	}

	Properties pr = new Properties();
	String caption;
	File file;

	for (int i = 0; i < files.length; i++) {
	    file = files[i];
	    ImageDescription d = cv.describeImageInStream(getBytes(file), null);
	    caption = d.captions().get(0).text();
	    pr.put("image." + i, caption);
	}

	return pr;
    }

    /**
     * @param file object representing image file
     * @return bytes object representing image data
     * @throws IOException in case file does not exist, or any other issue while
     *                     reading
     */
    private byte[] getBytes(File imageFile) throws IOException {
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
