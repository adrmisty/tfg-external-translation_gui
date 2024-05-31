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
import main.java.util.exception.ImageException;
import main.java.util.exception.ResourceException;
import main.java.util.resources.ResourceLoader;

/**
 * API access for image description functionality provided by Computer Vision
 * services.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class AzureApiVision implements ApiVision {

    private ComputerVision cv;
    private List<File> invalidFiles = new ArrayList<>();
    private List<File> validFiles = new ArrayList<>();

    public AzureApiVision() throws ResourceException {
	try {
	    ComputerVisionClient azureClient = ComputerVisionManager
		    .authenticate(ResourceLoader.getAzureVisionApiKey())
		    .withEndpoint(ResourceLoader.getAzureVisionEndpoint());
	    this.cv = azureClient.computerVision();
	} catch (Exception e) {
	    throw new ResourceException(e.getLocalizedMessage());
	}
    }

    @Override
    public Properties describe(List<File> files) {
	if (files.isEmpty()) {
	    return null;
	}

	// Process each image and save its results
	Properties pr = new Properties();
	String caption;
	ImageDescription d;

	int i = 0;
	for (File f : files) {
	    try {
		d = cv.describeImageInStream(getBytes(f), null);
		caption = d.captions().get(0).text();
		pr.put("image." + i, caption);
		i++;
	    } catch (Exception e) {
		// Image or IO exception, any: pass
		continue;
	    }
	}

	return pr;
    }

    @Override
    public List<File> validateImages(File[] files) throws ImageException {
	this.validFiles = new ArrayList<>();
	this.invalidFiles = new ArrayList<>();

	if (files != null) {
	    for (int i = 0; i < files.length; i++) {
		try {
		    File f = files[i];
		    if (validateImage(f)) {
			validFiles.add(f);
		    }
		} catch (ImageException e) {
		    continue;
		}
	    }

	    // At least one of the provided files is invalid
	    if (!this.invalidFiles.isEmpty()) {
		throw new ImageException(this.invalidFiles);
	    }
	}

	return validFiles;
    }

    @Override
    public List<File> getInvalidImages() {
	return invalidFiles;
    }

    @Override
    public List<File> getValidImages() {
	return validFiles;
    }

    /*
     * #########################################################################
     * Auxiliary methods
     * #########################################################################
     */

    /**
     * Confirms whether a given image is valid for automatic description, and
     * according to that decision, it is added to the list of valid files.
     * 
     * @param file object representing an image saved to a given path
     * @return whether valid or not
     * @throws ImageException in case of providing a file whose size does not
     *                        comply with minimum required pixels, or it cannot
     *                        be correctly read from disk
     */
    private boolean validateImage(File file) throws ImageException {
	try {
	    BufferedImage image = ImageIO.read(file);
	    int width = image.getWidth();
	    int height = image.getHeight();

	    if (width < 50 || height < 50) {
		this.invalidFiles.add(file);
		return false;
	    }

	    return true;
	} catch (IOException e) {
	    // In case of issues with input/output of images
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
