package speech;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.logic.speech.ApiSpeech;
import main.java.logic.speech.api.AzureApiSpeech;
import main.java.util.exception.SpeechException;

/**
 * TEXT-TO-SPEECH SUBSYSTEM TESTING
 * 
 * Project unit testing.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
@ExtendWith(MockitoExtension.class)
public class SpeechTests {

    private ResourceBundle messages;
    private ApiSpeech mockApiSpeech;

    @BeforeEach
    void setUp() {
	mockApiSpeech = Mockito.mock(ApiSpeech.class);
	Locale.setDefault(new Locale("en"));
	messages = ResourceBundle.getBundle("Messages", Locale.getDefault());
    }

    /**
     * TEST: AVAILABILITY OF 1 LANGUAGE CODE (region-specific)
     */
    @Test
    public void test_isAvailableFor_specific() {

	try {

	    // Checking availability of language
	    mockApiSpeech = new AzureApiSpeech(messages);

	    assertTrue(mockApiSpeech.isAvailableFor("es_ES"));

	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: AVAILABILITY OF 1 LANGUAGE CODE (global, regional will be spoken)
     */
    @Test
    public void test_isAvailableFor_global() {

	// Checking availability of language
	mockApiSpeech = new AzureApiSpeech(messages);

	assertTrue(mockApiSpeech.isAvailableFor("es"));
    }

    /**
     * TEST: AVAILABILITY OF 1 LANGUAGE CODE (false)
     */
    @Test
    public void test_isAvailableFor_unavailable() {

	// Checking availability of language
	mockApiSpeech = new AzureApiSpeech(messages);

	assertFalse(mockApiSpeech.isAvailableFor("xx_XX")); // region
	assertFalse(mockApiSpeech.isAvailableFor("xx")); // global
    }

    /**
     * TEST: SPEECH VIA THE API
     * 
     * @throws SpeechException
     */
    @Test
    void testSpeak() {
	String code = "en"; // global code
	Properties properties = new Properties();
	properties.put("label", "Testing speech...");

	mockApiSpeech.speak(code, properties);
	verify(mockApiSpeech).speak(eq(code), eq(properties));

	code = "en_US"; // global code
	mockApiSpeech.speak(code, properties);
	verify(mockApiSpeech).speak(eq(code), eq(properties));
    }

    /**
     * TEST: SPEECH VIA THE API
     * 
     * @throws SpeechException
     */
    @Test
    void testStop() {
	String code = "en"; // global code
	Properties properties = new Properties();
	properties.put("label", "Testing speech...");

	mockApiSpeech.speak(code, properties);
	verify(mockApiSpeech).speak(eq(code), eq(properties));
	mockApiSpeech.stop();
	verify(mockApiSpeech).stop();
    }

}
