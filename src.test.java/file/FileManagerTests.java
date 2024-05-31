package file;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.logic.file.FileManager;
import main.java.logic.file.locales.LocaleFile;
import main.java.logic.file.locales.SourceFile;
import main.java.util.exception.IncompleteResultsException;
import main.java.util.exception.PropertiesException;

/**
 * FILE MANAGEMENT SUBSYSTEM TESTING
 * 
 * Project unit testing.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class FileManagerTests {
    private FileManager fileManager;
    private ResourceBundle messages;
    private Properties content;

    // File paths
    private Map<String, String> files = new HashMap<>();
    private String sourcePath;
    private String targetPath;

    /*
     * SET UP ---
     */

    @BeforeEach
    void setUp() throws Exception {
	Locale.setDefault(new Locale("en"));
	messages = ResourceBundle.getBundle("Messages", Locale.getDefault());
	loadResources();
	content = new Properties();
	content.put("label.tfg", "Final Degree Project");
	content.put("label.tfg.description",
		"Outsourced translation service for programs, aimed at integral localization of files and automatic captioning and translation of images.");
	content.put("label.tfg.author",
		"Developed by Adriana Rodriguez Florez"); // UTF-8
	content.put("label.tfg.email", "Send an email!");
	fileManager = new FileManager(messages);
	sourcePath = files.get("Valid_en.properties");
	targetPath = "path/to/target/directory";
    }

    /**
     * Resource loading for tests.
     * 
     * - Loads file paths - Ensures UTF-8 loading
     */
    private void loadResources() {
	files.put("Valid_en.properties", getClass().getClassLoader()
		.getResource("file/Valid_en.properties").getPath());
	files.put("Valid_es_ES.properties", getClass().getClassLoader()
		.getResource("file/Valid_es_ES.properties").getPath());
	files.put("Invalid_en.properties", getClass().getClassLoader()
		.getResource("file/Invalid_en.properties").getPath());
	files.put("Invalid.txt", getClass().getClassLoader()
		.getResource("file/Invalid.txt").getPath());
	files.put("Invalid,es.properties", getClass().getClassLoader()
		.getResource("file/Invalid,es.properties").getPath());
    }

    /*
     * SET UP ---
     */

    /**
     * TEST: INPUT VALID FILE (en)
     * 
     * - A valid source file is used as input. * Valid filename * Valid
     * extension * Valid content * Valid code: global
     */
    @Test
    void testInput_globalValidFile() {
	content = new Properties();
	content.put("label.tfg", "Final Degree Project");
	content.put("label.tfg.description",
		"Outsourced translation service for programs, aimed at integral localization of files and automatic captioning and translation of images.");
	content.put("label.tfg.author",
		"Developed by Adriana Rodriguez Florez"); // UTF-8
	content.put("label.tfg.email", "Send an email!");

	try {
	    fileManager.input(sourcePath);

	    SourceFile source = (SourceFile) fileManager.getSourceFile();
	    assertNotNull(source);
	    assertEquals("Valid", source.getBundleName());
	    assertEquals("en", source.getCode());
	    assertEquals("English", source.getLanguage());
	    assertEquals(fileManager, source.getManager());
	    assertEquals(content, source.getContent());
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: INPUT VALID FILE (es_ES)
     * 
     * - A valid source file is used as input. * Valid filename * Valid
     * extension * Valid content * Valid code: region-specific
     */
    @Test
    void testInput_specificValidFile() {
	sourcePath = files.get("Valid_es_ES.properties");
	content = new Properties();
	content.put("label.tfg", "Trabajo de Fin de Grado");
	content.put("label.tfg.description",
		"Servicio de traduccion externalizado para programas, como asistente integral para accesibilidad y localizacion.");
	content.put("label.tfg.author",
		"Desarrollado por Adriana Rodriguez Florez"); // UTF-8
	content.put("label.tfg.email", "Envia un correo electronico");

	try {
	    fileManager.input(sourcePath);

	    SourceFile source = (SourceFile) fileManager.getSourceFile();
	    assertNotNull(source);
	    assertEquals("Valid", source.getBundleName());
	    assertEquals("es_ES", source.getCode());
	    assertEquals("Spanish (Spain)", source.getLanguage());
	    assertEquals(fileManager, source.getManager());
	    assertEquals(content, source.getContent());
	} catch (Exception e) {
	    e.printStackTrace();
	    fail();
	}
    }

    /**
     * TEST: INPUT INVALID FILE (wrong extension)
     * 
     * Expected: PropertiesException
     */
    @Test
    void test_inputWrongExtensionFile() {
	assertThrows(PropertiesException.class, () -> {
	    fileManager.input(files.get("Invalid.txt"));
	});
    }

    /**
     * TEST: INPUT INVALID FILE (wrong filename)
     * 
     * Expected: PropertiesException
     */
    @Test
    void test_inputWrongNameFile() {
	assertThrows(PropertiesException.class, () -> {
	    fileManager.input(files.get("Invalid,es.properties"));
	});
    }

    /**
     * TEST: INPUT INVALID FILE (wrong content)
     * 
     * Expected: PropertiesException
     */
    @Test
    void test_inputWrongContentFile() {
	assertThrows(PropertiesException.class, () -> {
	    fileManager.input(files.get("Invalid_en.properties"));
	});
    }

    /**
     * TEST: ADD A NEW LANGUAGE (non-default)
     */
    @Test
    void test_newTargetFile() {
	sourcePath = files.get("Valid_en.properties");
	try {
	    fileManager.input(sourcePath);

	    String language = "Spanish";
	    LocaleFile targetFile = fileManager.newLanguage(language, false);
	    assertNotNull(targetFile);
	    assertTrue(!targetFile.isDefault());
	    assertNull(targetFile.getContent());
	    assertEquals("es", targetFile.getCode());
	    assertEquals("Valid", targetFile.getBundleName());
	    assertEquals(language, targetFile.getLanguage());
	    assertTrue(fileManager.getTargetFiles().contains(targetFile));
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: ADD A NEW LANGUAGE (default)
     */
    @Test
    void test_newDefaultTargetFile() {
	sourcePath = files.get("Valid_en.properties");
	try {
	    fileManager.input(sourcePath);

	    String language = "Spanish";
	    LocaleFile targetFile = fileManager.newLanguage(language, true);
	    assertNotNull(targetFile);
	    assertTrue(targetFile.isDefault());
	    assertNull(targetFile.getContent());
	    assertEquals("es", targetFile.getCode());
	    assertEquals("Valid", targetFile.getBundleName());
	    assertEquals(language, targetFile.getLanguage());
	    assertTrue(fileManager.getTargetFiles().contains(targetFile));
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: SELECTING SAVE DIRECTORY FOR TARGET FILES
     */
    @Test
    void test_toTargetPath() {
	sourcePath = files.get("Valid_en.properties");
	try {
	    fileManager.input(sourcePath);
	    fileManager.newLanguage("Spanish", false);
	    fileManager.newLanguage("French", true);
	    fileManager.to(targetPath);

	    assertEquals(targetPath, fileManager.getTargetPath());
	    for (LocaleFile file : fileManager.getTargetFiles()) {
		assertEquals(targetPath + "/" + file.getFullName(),
			file.getPath());
		if (file.isDefault()) { // No code
		    assertFalse(file.getPath().contains("_"));
		}
	    }
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: SAVE WRITTEN RESULTS TO PATH DEFINED BY "TO"
     */
    @Test
    void test_saveAll() {
	sourcePath = files.get("Valid_es_ES.properties");

	try {
	    fileManager.input(sourcePath);
	    fileManager.newLanguage("English", false);
	    fileManager.getTargetFiles().get(0).setContent(content);
	    fileManager.to(targetPath);

	    List<String> savedPaths = fileManager.saveAll();
	    assertNotNull(savedPaths);
	    assertEquals(1, savedPaths.size());
	    assertTrue(savedPaths.get(0).contains(targetPath));

	    // Verify the file has been written in the directory
	    Path filePath = Paths.get(savedPaths.get(0));
	    assertTrue(Files.exists(filePath));

	    // Eliminate from path
	    Files.delete(filePath);
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: GET RESULT PATHS
     */
    @Test
    void test_resultsPaths() {
	sourcePath = files.get("Valid_es_ES.properties");

	try {
	    fileManager.input(sourcePath);
	    fileManager.newLanguage("English", false);
	    fileManager.getTargetFiles().get(0).setContent(content);
	    fileManager.to(targetPath);

	    fileManager.saveAll();
	    List<String> results = fileManager.getResultsPaths();
	    assertNotNull(results);
	    assertEquals(1, results.size());
	    assertEquals(
		    targetPath + "/"
			    + fileManager.getTargetFiles().get(0).getFullName(),
		    results.get(0));
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: REVIEW TEMPORARY FILES
     */
    @Test
    void test_review() {
	sourcePath = files.get("Valid_es_ES.properties");

	try {
	    fileManager.input(sourcePath);
	    fileManager.newLanguage("English", false);
	    fileManager.getTargetFiles().get(0).setContent(content);
	    List<String> reviewPaths = fileManager.review();
	    assertNotNull(reviewPaths);
	    assertEquals(1, reviewPaths.size());

	    // Is temporary
	    assertTrue(reviewPaths.get(0)
		    .contains(System.getProperty("java.io.tmpdir")));

	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: ARE RESULTS COMPLETE (true)
     * 
     * @throws PropertiesException
     * @throws IOException
     * @throws IncompleteResultsException
     */
    @Test
    void test_completeResults() {
	sourcePath = files.get("Valid_es_ES.properties");

	try {
	    fileManager.input(sourcePath);
	    fileManager.newLanguage("English", false);
	    fileManager.getTargetFiles().get(0).setContent(content);
	    assertTrue(fileManager.areResultsComplete());
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: ARE RESULTS COMPLETE (false)
     * 
     * Expected: IncompleteResultsException
     */
    @Test
    void test_incompleteResults() {
	sourcePath = files.get("Valid_es_ES.properties");

	try {
	    fileManager.input(sourcePath);
	    fileManager.newLanguage("English", false);
	    content.remove("label.tfg.author"); // One of the values has not
						// been translated
	    fileManager.getTargetFiles().get(0).setContent(content);
	    assertThrows(IncompleteResultsException.class, () -> {
		fileManager.areResultsComplete();
	    });
	} catch (Exception e) {
	    fail();
	}
    }

    /**
     * TEST: INCLUDE (image description cases)
     */
    @Test
    void test_includeImageDescription() {
	sourcePath = files.get("Valid_es_ES.properties");

	try {
	    fileManager.input(sourcePath);
	    fileManager.newLanguage("English", false);
	    fileManager.getTargetFiles().get(0).setContent(content);

	    Properties properties = new Properties();
	    properties.put("image.0", "a dog sitting down");
	    fileManager.include(properties);

	    assertEquals("a dog sitting down",
		    fileManager.getSourceFile().getContent().get("image.0"));
	} catch (Exception e) {
	    fail();
	}

    }
}
