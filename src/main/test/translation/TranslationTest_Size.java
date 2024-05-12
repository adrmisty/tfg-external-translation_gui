package translation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
 * Checks whether completion of translation results is managed correctly: in
 * case the API returns incomplete results, the properties content should be
 * filled with the respective remaining keys and an empty result.
 * 
 * @author Adriana R.F. - UO282798@uniovi.es
 * @version May 2024
 */
public class TranslationTest_Size {

    // Automatically instantiates
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
	english = new Properties();

	for (int i = 0; i < 100; i++) {
	    spanish.put("label" + i, "Ejemplo de frase larga");
	    if (i < 50) {
		english.put("label" + i, "Long sentence example");
	    } else {
		english.put("label" + i, "");
	    }
	}

	// Mock API
	// Create a mock for the translation API (based on OpenAI)
	mockApi = Mockito.mock(OpenAIApiTranslation.class);
	api = new OpenAIApiTranslation(mockApi);
    }

    @Test
    public void testApiTranslation_Spanish2English() {

	System.out.println("\nSpanish - English test (large file).");

	try {
	    when(mockApi.translate(spanish, "English")).thenReturn(english);
	    Properties results = api.translate(spanish, "English");
	    verify(mockApi).translate(spanish, "English");

	    boolean checked = false;
	    String translated = "";
	    int empty = 0;
	    for (String v : PropertiesUtil.getValues(results)) {
		if (v.isBlank()) {
		    empty++;
		} else if (!checked) {
		    translated = v;
		    checked = true;
		}
	    }

	    String nonEmptyCount = String.format(
		    "Expected size: [%d] - Returned size: [%d]", spanish.size(),
		    results.size());
	    String emptyCount = String.format(
		    "Expected empty results: [0] - Returned empty results: [%d]",
		    empty);
	    String result = String.format(
		    "Expected: [Long sentence example] - Returned: [%s]",
		    translated);

	    System.out.println(result);
	    System.out.println(emptyCount);
	    System.out.println(nonEmptyCount);

	    // Verify the translated text
	    assertEquals(english.size(), results.size(),
		    "Results size: incorrect.\n");
	    assertEquals("Long sentence example", translated,
		    "Translation results: incorrect.\n");

	} catch (TranslationException e) {
	    fail("This case should not arise an exception");
	}
    }

}