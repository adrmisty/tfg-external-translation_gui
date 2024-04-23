package main.java.logic.image.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVision;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageDescription;

import main.java.logic.image.ApiVision;
import main.java.logic.util.exception.ImageException;
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
    private List<File> invalidFiles = new ArrayList<>();

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
    public Properties caption() throws ImageException {
	if (this.files == null) {
	    return null;
	}

	this.invalidFiles = new ArrayList<>();
	Properties pr = new Properties();
	String caption;
	File file;

	for (int i = 0; i < files.length; i++) {
	    file = files[i];
	    validateImage(file);
	    ImageDescription d = cv.describeImageInStream(getBytes(file), null);
	    caption = d.captions().get(0).text();
	    pr.put("image." + i, caption);
	}

	if (!this.invalidFiles.isEmpty()) {
	    throw new ImageException(invalidFiles);
	}

	return pr;
    }

    /**
     * Confirms whether a given image is valid for automatic description.
     * 
     * @param file file object representing an image
     * @return int array containing [width,height] in pixels
     * @throws ImageException in case of providing a file whose size does not
     *                        comply with minimum required pixels, or it cannot
     *                        be correctly read from disk
     */
    private int[] validateImage(File file) throws ImageException {
	try {
	    BufferedImage image = ImageIO.read(file);
	    int width = image.getWidth();
	    int height = image.getHeight();

	    if (width < 50 || height > 50) {
		this.invalidFiles.add(file);
	    }

	    return new int[] { width, height };
	} catch (IOException e) {
	    throw new ImageException();
	}
    }

    /**
     * @param file object representing image file
     * @return bytes object representing image data
     * @throws IOException in case file does not exist, or any other issue while
     *                     reading
     */
    private byte[] getBytes(File imageFile) throws ImageException {

	FileInputStream inputStream = null;

	try {
	    // Byte array to store the image data
	    inputStream = new FileInputStream(imageFile);
	    long size = imageFile.length();
	    byte[] bytes = new byte[(int) size];

	    // Read the image data into the byte array
	    int read = inputStream.read(bytes);
	    if (read != size) {
		throw new ImageException();
	    }
	    inputStream.close();

	    return bytes;
	} catch (FileNotFoundException e) {
	    throw new ImageException();
	} catch (IOException e) {
	    throw new ImageException();
	} finally {
	    try {
		inputStream.close();
	    } catch (IOException e) {
		throw new ImageException();
	    }
	}
    }

}
