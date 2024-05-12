package translation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import main.java.logic.translation.api.ApiTranslation;
import main.java.logic.translation.api.openai.OpenAIApiTranslation;
import main.java.util.exception.TranslationException;
import main.java.util.properties.PropertiesUtil;

/**
 * Basic mock testing for the OpenAI translation API's results.
 * 
 * Checks whether a basic English-Spansih translation task is executed properly.
 * The results must be complete and there must be no exceptions arising.
 * 
 * @author Adriana R.F. - UO282798@uniovi.es
 * @version May 2024
 */
public class TranslationTest_Results {

    @InjectMocks
    ApiTranslation api;
    @Mock
    ApiTranslation mockApi;

    private Properties spanish;
    private Properties english;

    @BeforeEach
    void setUp() {
	// Initialize properties examples
	spanish = new Properties();
	spanish.putAll(Map.of("label1", "Hola", "label2",
		"Mi nombre es Adriana", "label3", "Tengo 21 años"));
	english = new Properties();
	english.putAll(Map.of("label1", "Hello", "label2", "My name is Adriana",
		"label3", "I am 21 years old"));

	// Mock API
	// Create a mock for the translation API (based on OpenAI)
	mockApi = Mockito.mock(OpenAIApiTranslation.class);
	api = new OpenAIApiTranslation(mockApi);
    }

    @Test
    public void testApiTranslation_Spanish2English() {

	System.out.println("\nSpanish - English test.");

	try {
	    when(mockApi.translate(spanish, "English")).thenReturn(english);
	    Properties results = api.translate(spanish, "English");
	    verify(mockApi).translate(spanish, "English");

	    String strResults = String.format("Expected: [%s] - Returned: [%s]",
		    String.join(" - ", PropertiesUtil.getValues(english)),
		    String.join(" - ", PropertiesUtil.getValues(results)));

	    System.out.println(strResults);
	    // Verify the translated text
	    assertEquals(english, results, "Translation results: incorrect.\n");
	    System.out.println("Translation results: correct.\n");

	} catch (TranslationException e) {
	    fail("This case should not arise an exception");
	}
    }

    @Test
    public void testApiTranslation_English2Spanish() {

	System.out.println("\nEnglish - Spanish test.");

	try {
	    when(mockApi.translate(english, "Spanish")).thenReturn(spanish);
	    Properties results = api.translate(english, "Spanish");
	    verify(mockApi).translate(english, "Spanish");

	    String strResults = String.format("Expected: [%s] - Returned: [%s]",
		    String.join(" - ", PropertiesUtil.getValues(spanish)),
		    String.join(" - ", PropertiesUtil.getValues(results)));

	    System.out.println(strResults);
	    // Verify the translated text
	    assertEquals(spanish, results, "Translation results: incorrect.\n");
	    System.out.println("Translation results: correct.\n");

	} catch (TranslationException e) {
	    fail("This case should not arise an exception");
	}
    }

}