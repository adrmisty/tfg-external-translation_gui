package image;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import main.java.logic.image.ApiVision;
import main.java.logic.image.Vision;
import main.java.util.exception.ImageException;
import main.java.util.properties.PropertiesUtil;

/**
 * Basic mock testing for the Microsoft Azure Vision results.
 * 
 * Checks whether basic image description functionalities are executed
 * correctly.
 * 
 * @author Adriana R.F. - UO282798@uniovi.es
 * @version May 2024
 */
public class ImageTest_Results {

    private final static String OK_IMAGE = "/test/test-ok.jpg";
    private final static String ERR_IMAGE = "/test/test-small.png";

    @InjectMocks
    private Vision vision;
    @Mock
    private ApiVision mockApi;

    private Properties captions;
    private File[] validImages = new File[1];
    private File[] invalidImages = new File[1];

    @Before
    public void setUp() throws URISyntaxException {
	// Initialize properties examples
	captions = new Properties();
	captions.putAll(Map.of("image.0", "a dog lying on the ground"));

	// Test images
	validImages[0] = new File(
		ImageTest_Results.class.getResource(OK_IMAGE).toURI());
	validImages[0] = new File(
		ImageTest_Results.class.getResource(ERR_IMAGE).toURI());

	// Mock API
	// Create a mock for the translation API (based on OpenAI)
	mockApi = Mockito.mock(ApiVision.class);
	vision = new Vision(mockApi);
    }

    /**
     * Carries out setting and automatic description for a valid image.
     */
    @Test
    public void testApiVision_Valid() {

	System.out.println("\nValid image test.");

	try {
	    // Mock api settings
	    when(mockApi.caption(validImages)).thenReturn(captions);

	    // Actual results
	    Properties results = vision.captions(validImages);
	    verify(mockApi).caption(validImages);
	    ;

	    String strResults = String.format("Expected: [%s] - Returned: [%s]",
		    String.join(" - ", PropertiesUtil.getValues(captions)),
		    String.join(" - ", PropertiesUtil.getValues(results)));

	    System.out.println(strResults);
	    // Verify the translated text
	    assertEquals(captions, results, "Caption results: incorrect.\n");

	} catch (ImageException e) {
	    fail("This case should not arise an [Image] exception");
	} catch (Exception e) {
	    fail("This case should not arise an exception");
	}
    }

    /**
     * Arises an Image Exception for setting an image that does not comply with
     * minimum dimension requirements (50x50 pixels).
     */
    @Test
    public void testApiVision_Invalid() {

	System.out.println("\nInvalid image test.");

	try {
	    when(mockApi.setImages(invalidImages))
		    .thenThrow(ImageException.class);

	    assertThrows(ImageException.class,
		    () -> vision.setImages(invalidImages));
	    System.out.println("Image exception for an image < 50x50 pixels.");
	} catch (Exception e) {
	    // Nothing
	}

    }

}