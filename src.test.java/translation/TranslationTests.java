package translation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import main.java.logic.file.locales.LocaleFile;
import main.java.logic.translation.TranslationManager;
import main.java.logic.translation.api.ApiTranslation;
import main.java.logic.translation.cache.TranslationCache;
import main.java.logic.translation.mode.AutoTranslation;
import main.java.util.exception.ResourceException;

/**
 * TRANSLATION MANAGER TESTING
 * 
 * Project unit testing.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class TranslationTests {
    private TranslationManager translator;
    private ResourceBundle messages;
    private Properties spanish;
    private Map<String, String> files = new HashMap<>();

    // Mocks for auto translation
    private ApiTranslation api;
    private AutoTranslation auto;
    private TranslationCache cache;
    private Connection connection;
    private final static String DATABASE_NAME = "/database/test_cache.db";

    @BeforeEach
    void setUp() throws Exception {
	Locale.setDefault(new Locale("en"));
	messages = ResourceBundle.getBundle("Messages", Locale.getDefault());
	loadResources();

	// Automatic translation
	translator = new TranslationManager(messages);
	api = Mockito.mock(ApiTranslation.class);
	connection = DriverManager.getConnection(getJdbcUrl());
	cache = new TranslationCache(connection, getJdbcUrl());
    }

    private String getJdbcUrl() {
	try {
	    URI jdbc = getClass().getResource(DATABASE_NAME).toURI();
	    return "jdbc:sqlite:" + Paths.get(jdbc).toString();
	} catch (URISyntaxException e) {
	    throw new ResourceException(DATABASE_NAME);
	}
    }

    @AfterEach
    public void tearDown() throws Exception {
	cache.reset();
	cache.closeConnection();
	connection.close();
    }

    /**
     * Resource loading for tests.
     * 
     * - Loads file paths - Ensures UTF-8 loading
     */
    private void loadResources() {
	files.put("Valid_en.properties", getClass().getClassLoader()
		.getResource("file/Valid_en.properties").getPath());
	spanish = new Properties();
	spanish.put("label.tfg", "Trabajo de Fin de Grado");
	spanish.put("label.tfg.description",
		"Servicio externalizado de traducción de programas.");
	spanish.put("label.tfg.author",
		"Desarrollado por Adriana Rodriguez Florez"); // UTF-8
	spanish.put("label.tfg.email", "Manda un email!");

    }

    /**
     * TEST: SET TARGET LANGUAGES
     * 
     * - Set a selection of 3 target languages, 1 of them default
     */
    @Test
    public void test_selectTargetLanguages() {
	try {
	    translator.input(files.get("Valid_en.properties"));
	    translator.setManualMode();

	    // Select languages with format user views (different from file
	    // manager format)
	    assertDoesNotThrow(() -> translator.setTargetLanguages(
		    List.of("English", "Spanish, Spain", "French, France"),
		    "English"));

	    List<LocaleFile> targets = translator.getTargetFiles();

	    LocaleFile defEnglish = targets.get(0);
	    LocaleFile spanish = targets.get(1);
	    LocaleFile french = targets.get(2);

	    assertEquals("English", defEnglish.getLanguage());
	    assertEquals("Spanish (Spain)", spanish.getLanguage());
	    assertTrue(defEnglish.isDefault());
	    assertEquals("French (France)", french.getLanguage());
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: SET TARGET LANGUAGES
     * 
     * - Set a selection of 3 target languages, 1 of them default
     */
    @Test
    public void test_resetTargetLanguages() {
	try {
	    translator.input(files.get("Valid_en.properties"));
	    translator.setManualMode();
	    assertDoesNotThrow(() -> translator.setTargetLanguages(
		    List.of("English", "Spanish, Spain", "French, France"),
		    "English"));
	    assertEquals(3, translator.getTargetFiles().size());

	    translator.to("path/to/target/files");
	    translator.resetLanguages();

	    assertTrue(translator.getTargetFiles().isEmpty());
	    assertTrue(translator.getResultsPaths().isEmpty());

	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: SET AUTO & MANUAL MODE
     */
    @Test
    public void test_selectMode() {
	try {
	    translator.input(files.get("Valid_en.properties"));
	    assertDoesNotThrow(() -> {
		translator.setAutoMode();
	    });
	    assertDoesNotThrow(() -> {
		translator.setManualMode();
	    });
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: MANUAL TRANSLATION
     */
    @Test
    public void test_manualTranslation() {
	try {
	    translator.input(files.get("Valid_en.properties"));
	    translator.setManualMode();
	    assertDoesNotThrow(() -> translator
		    .setTargetLanguages(List.of("Spanish, Spain"), null));

	    List<LocaleFile> targets = translator.getTargetFiles();
	    LocaleFile spanishLang = targets.get(0);

	    spanishLang.setContent(spanish);

	    assertTrue(translator.isDone());
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: AUTOMATIC TRANSLATION FROM TRANSLATION MANAGER
     */
    @Test
    public void test_autoTranslation_fromApi() {
	try {
	    translator.input(files.get("Valid_en.properties"));
	    LocaleFile sourceFile = translator.getSource();

	    auto = new AutoTranslation(sourceFile, api, cache);
	    translator.setAutoMode(auto);

	    assertDoesNotThrow(() -> {
		translator.setTargetLanguages(List.of("Spanish, Spain"), null);
	    });

	    when(api.translate(sourceFile.getContent(),
		    sourceFile.getLanguage(), "Spanish (Spain)"))
		    .thenReturn(spanish);
	    when(api.getResults()).thenReturn(spanish);

	    // translator.translateAll() --> api.translate(sourceFile,...)
	    translator.translateAll();

	    assertEquals(translator.getTargetFiles().get(0).getContent(),
		    spanish);
	    assertTrue(translator.isDone());
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: AUTOMATIC TRANSLATION FROM TRANSLATION MANAGER (case: user
     * translates to variations of the same language)
     */
    @Test
    public void test_autoTranslation_sameLanguage() {
	try {
	    translator.input(files.get("Valid_en.properties"));
	    LocaleFile sourceFile = translator.getSource();

	    auto = new AutoTranslation(sourceFile, api, cache);
	    translator.setAutoMode(auto);

	    assertDoesNotThrow(() -> {
		translator.setTargetLanguages(List.of("English, United States",
			"English, United Kingdom", "English"), null);
	    });

	    translator.translateAll();

	    assertEquals(translator.getTargetFiles().get(0).getContent(),
		    sourceFile.getContent());
	    assertEquals(translator.getTargetFiles().get(1).getContent(),
		    sourceFile.getContent());
	    assertEquals(translator.getTargetFiles().get(2).getContent(),
		    sourceFile.getContent());

	    assertTrue(translator.isDone());
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: AUTOMATIC TRANSLATION FROM TRANSLATION MANAGER (case: translation
     * stored in database)
     */
    @Test
    public void test_autoTranslation_fromCache() {
	try {
	    translator.input(files.get("Valid_en.properties"));
	    LocaleFile sourceFile = translator.getSource();

	    // Store translations into Spanish
	    cache.storeAll(spanish, sourceFile.getContent(), "es");
	    auto = new AutoTranslation(sourceFile, api, cache);
	    translator.setAutoMode(auto);

	    assertDoesNotThrow(() -> {
		translator.setTargetLanguages(List.of("Spanish"), null);
	    });

	    // Verify the api is not called because results are found in cache
	    // already
	    translator.translateAll();

	    // Verify contents of target file
	    assertEquals(translator.getTargetFiles().get(0).getContent(),
		    spanish);
	    assertTrue(translator.isDone());
	} catch (Exception e) {
	    fail();
	}
    }

}
