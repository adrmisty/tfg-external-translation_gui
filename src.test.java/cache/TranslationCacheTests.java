package cache;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.logic.translation.cache.TranslationCache;
import main.java.util.exception.ResourceException;
import main.java.util.resources.HashUtil;

/**
 * DATABASE MANAGEMENT TESTING
 * 
 * Project unit testing.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class TranslationCacheTests {

    private TranslationCache cache;
    private Connection connection;
    private final static String DATABASE_NAME = "/database/test_cache.db";

    /**
     * SET UP AND TEAR DOWN -----
     */

    @BeforeEach
    public void setUp() throws Exception {
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
     * SET UP AND TEAR DOWN -----
     */

    /**
     * TEST: STORE ALL TRANSLATIONS
     */
    @Test
    public void test_storeTranslations() throws Exception {
	Properties original = new Properties();
	original.setProperty("label", "Good night");
	original.setProperty("label2", "Good morning");
	Properties translated = new Properties();
	translated.setProperty("label", "Buenas noches");
	translated.setProperty("label2", "Buenos dias");
	String language = "es";

	// Store translation-original relation for a given language
	cache.storeAll(translated, original, language);

	// Directly check the database to ensure the translation was stored
	try (Statement stmt = connection.createStatement()) {

	    // First label
	    ResultSet rs = stmt.executeQuery(
		    "SELECT * FROM translation_cache WHERE text_hash = '"
			    + HashUtil.getHash(original.getProperty("label"))
			    + "' AND language_code = '" + language + "'");
	    assertTrue(rs.next());
	    assertEquals(translated.get("label"),
		    rs.getString("text_translation"));

	    // Second label
	    rs = stmt.executeQuery(
		    "SELECT * FROM translation_cache WHERE text_hash = '"
			    + HashUtil.getHash(original.getProperty("label2"))
			    + "' AND language_code = '" + language + "'");
	    assertTrue(rs.next());
	    assertEquals(translated.get("label2"),
		    rs.getString("text_translation"));
	}

    }

    /**
     * TEST: STORE ALL TRANSLATIONS & RETRIEVE VIA MATCH
     */
    @Test
    public void test_retrieveTranslation() throws Exception {
	Properties original = new Properties();
	Properties translated = new Properties();
	translated.setProperty("label", "Omelette du fromage");
	original.setProperty("label", "Tortilla de queso");
	String language = "fr_FR";

	// Store translation-original relation for a given language
	cache.storeAll(translated, original, language);

	// Retrieve from DB
	try (Statement stmt = connection.createStatement()) {
	    ResultSet rs = stmt.executeQuery(
		    "SELECT * FROM translation_cache WHERE text_hash = '"
			    + HashUtil.getHash(original.getProperty("label"))
			    + "' AND language_code = '" + language.toLowerCase()
			    + "'");
	    assertTrue(rs.next());
	    assertEquals(translated.get("label"),
		    rs.getString("text_translation"));
	}

	// Use match
	cache.match(original, language);
	String retrievedTranslation = cache.getTranslated()
		.getProperty("label");
	assertEquals(translated.get("label"), retrievedTranslation);

    }

    /**
     * TEST: STORE ALL TRANSLATIONS & RETRIEVE VIA MATCH WITH UNCACHED
     * TRANSLATIONS
     */
    @Test
    public void test_matchCachedTranslations() throws Exception {
	Properties original = new Properties();
	Properties translated = new Properties();
	translated.setProperty("label", "Omelette du fromage");
	original.setProperty("label", "Tortilla de queso");
	String language = "fr_FR";

	// Simulate having stored one of them beforehand
	cache.storeAll(translated, original, language);

	// Add some more
	original.put("label2", "Queso gruyere");

	cache.match(original, language);

	Properties cached = cache.getTranslated();
	Properties uncached = cache.getUntranslated();

	assertEquals(1, cached.size());
	assertEquals(cached.getProperty("label"),
		translated.getProperty("label"));
	assertNull(cached.getProperty("label2"));
	assertNotNull(uncached.getProperty("label2"));
	assertEquals(1, uncached.size());
    }

    /**
     * TEST: RESET THE DATABASE
     */
    @Test
    public void test_resetDatabase() throws Exception {
	Properties original = new Properties();
	Properties translated = new Properties();
	translated.setProperty("label", "Omelette du fromage");
	original.setProperty("label", "Tortilla de queso");
	String language = "fr_FR";

	cache.storeAll(translated, original, language);

	cache.reset();

	try (Statement stmt = connection.createStatement()) {
	    ResultSet rs = stmt.executeQuery(
		    "SELECT * FROM translation_cache WHERE text_hash = '"
			    + HashUtil.getHash(original.getProperty("label"))
			    + "' AND language_code = '" + language + "'");
	    assertFalse(rs.next());
	}
    }

}
