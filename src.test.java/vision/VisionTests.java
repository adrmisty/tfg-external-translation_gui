package vision;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.logic.image.ApiVision;
import main.java.logic.image.Vision;
import main.java.logic.image.api.AzureApiVision;
import main.java.util.exception.ImageException;

/**
 * IMAGE DESCRIPTION SUBSYSTEM TESTING
 * 
 * Project unit testing.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
@ExtendWith(MockitoExtension.class)
public class VisionTests {

    private Vision vision;
    private ApiVision mockApiVision;
    private Map<String, File> files = new HashMap<>();

    @BeforeEach
    void setUp() {
	mockApiVision = Mockito.mock(ApiVision.class);
	vision = new Vision(mockApiVision);
	loadResources();
    }

    /**
     * Resource loading for tests.
     * 
     * - Loads file paths - Ensures UTF-8 loading
     */
    private void loadResources() {
	files.put("Dog", new File(getClass().getClassLoader()
		.getResource("img/test-dog.jpg").getPath()));
	files.put("Cat", new File(getClass().getClassLoader()
		.getResource("img/test-dog.jpg").getPath()));
	files.put("Error", new File(getClass().getClassLoader()
		.getResource("img/test-small.png").getPath()));
    }

    /**
     * TEST: IMAGE DESCRIPTION OF 2 VALID IMAGES
     */
    @Test
    public void test_imageDescription_2valid() {
	Properties mockDescriptions = new Properties();
	mockDescriptions.put("image.0", "a dog sitting down");
	mockDescriptions.put("image.1", "a cat sitting down");

	try {
	    List<File> validImages = new ArrayList<>();
	    validImages.add(files.get("Dog"));
	    validImages.add(files.get("Cat"));
	    File[] images = validImages.toArray(new File[2]);

	    // Stubbing
	    when(mockApiVision.validateImages(images)).thenReturn(validImages);
	    when(mockApiVision.describe(validImages))
		    .thenReturn(mockDescriptions);

	    // Executing
	    vision.setImages(images);
	    assertEquals(mockDescriptions, vision.describe());

	    // Verifying
	    verify(mockApiVision, times(1)).describe(validImages);
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: IMAGE DESCRIPTION OF 1 INVALID IMAGE
     */
    @Test
    public void test_imageDescription_1invalid() {

	File[] images = new File[] { files.get("Error") };
	List<File> invalidImages = new ArrayList<>();
	invalidImages.add(files.get("Error"));

	try {

	    // Image processing throws exception
	    mockApiVision = new AzureApiVision();
	    vision = new Vision(mockApiVision);

	    Exception e = assertThrows(ImageException.class, () -> {
		vision.setImages(images);
	    });
	    assertEquals(invalidImages, ((ImageException) e).getInvalidFiles());

	    // Results are null
	    assertNull(vision.describe());

	} catch (Exception e) {
	    fail();
	}
    }

}
