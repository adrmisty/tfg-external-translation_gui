package main.java.logic.translation.cache;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import main.java.util.exception.TranslationException;
import main.java.util.resources.HashUtil;
import main.java.util.resources.PropertyLoader;
import main.java.util.resources.ResourceLoader;

/**
 * Builds and manages a database (SQLite) aimed at acting like a caché for
 * requested and returned translations from the API, in order to alleviate API
 * usage and speed up the execution of the program.
 * 
 * This caché interacts with a very basic database, containing just one table,
 * which stores the hashcode of a text to translate, language it has been
 * translated to and its effective translation.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class TranslationCache {

    // Connection to database
    private String JDBC_URL;
    private Connection connection;

    // Translations in cache
    private Properties notInCache; // Not found in cache
    private Properties inCache; // Translations found in the cache

    // Queries
    // No over-design, just hardcode them in here (we just need 2!)
    private String FIND = "SELECT text_translation as text FROM translation_cache WHERE text_hash=? and language_code=?";
    private String INSERT = "INSERT INTO translation_cache (text_hash, text_translation,"
	    + " created_at, language_code) VALUES (?, ?, ?, ?)";
    private String DELETE = "DELETE FROM translation_cache";
    private String CREATE = "CREATE TABLE IF NOT EXISTS translation_cache ("
	    + "text_hash TEXT NOT NULL, " + "text_translation TEXT, "
	    + "created_at TIMESTAMP, " + "language_code TEXT NOT NULL, "
	    + "PRIMARY KEY (text_hash, language_code))";
    private String DROP = "DROP TABLE IF EXISTS translation_cache";

    public TranslationCache() throws SQLException {
	getConnection();
	setUp();
    }

    public TranslationCache(Connection c, String URL) throws SQLException {
	this.connection = c;
	this.JDBC_URL = URL;
	setUp();
    }

    /**
     * Sets up and creates the database.
     * 
     * @throws SQLException
     */
    private void setUp() throws SQLException {

	// Reset
	try (Statement stmt = getConnection().createStatement()) {
	    stmt.executeUpdate(DROP);
	} catch (Exception e) {
	    return;
	}

	// Create table from scratch
	try (Statement stmt = getConnection().createStatement()) {
	    stmt.executeUpdate(CREATE);
	} catch (Exception e) {
	    return;
	}
    }

    /**
     * @return set of properties that are not translated and thus, not found in
     *         the database
     */
    public Properties getUntranslated() {
	return notInCache;
    }

    /**
     * @return set of properties (in a given language) that match those given as
     *         input and are found in the database
     */
    public Properties getTranslated() {
	return inCache;
    }

    /**
     * Closes the connection to the cache database (if existing).
     */
    public void closeConnection() {
	try {
	    if (connection != null && !connection.isClosed()) {
		connection.close();
	    }
	} catch (Exception e) {
	    return;
	}
    }

    /**
     * Stores all translations of the submitted textual queries, to their
     * respective target language.
     * 
     * @param results:   all translations
     * @param originals: original, untranslated texts
     * @param language:  alpha2 code of the target language
     * 
     * @return boolean true if all results have been correctly stored
     */
    public boolean storeAll(Properties results, Properties originals,
	    String language) {

	boolean allStored = true;
	for (String k : PropertyLoader.getKeys(results)) {
	    String text = originals.getProperty(k);
	    String translation = results.getProperty(k);
	    if (!translation.isEmpty()) {
		// Will only be stored if not already found in the DB
		if (!store(text, translation, language)) {
		    allStored = false;
		}
	    }
	}

	return allStored;
    }

    /**
     * Deletes all records from the database.
     * 
     * @throws SQL exception
     */
    public void reset() throws SQLException {
	try (PreparedStatement stmnt = getConnection()
		.prepareStatement(DELETE)) {
	    stmnt.executeUpdate();
	}
    }

    /**
     * Retrieves all present translations of a given set of properties into a
     * language, that are in the database; and also those that are not cached.
     * 
     * @param properties object containing set of translations to be fully
     *                   computed or not
     * @param language   alpha2 code of the language @ in case of error with
     *                   database access, writing to/from...
     * @throws TranslationException
     */
    public void match(Properties properties, String language)
	    throws TranslationException {

	// Initialize
	this.inCache = new Properties();
	this.notInCache = new Properties();

	// Retrieve translations
	List<String> keys = PropertyLoader.getKeys(properties);

	for (String key : keys) {
	    String text = properties.getProperty(key);
	    String translation = getTranslation(text, language);

	    if (translation != null) {
		this.inCache.put(key, translation);
	    } else {
		this.notInCache.put(key, text);
	    }
	}
    }

    /*
     * ######################## AUXILIARY METHODS ##############################
     */

    /**
     * Establishes a connection to the SQLite database.
     * 
     * If not possible, rather than throwing a SQLException, no access is done
     * to the database.
     * 
     * @return established connection to the SQLite database
     */
    private Connection getConnection() {
	try {
	    if (JDBC_URL == null) {
		JDBC_URL = ResourceLoader.getJdbcUrl();
	    }

	    if (connection == null || connection.isClosed()) {
		connection = DriverManager.getConnection(JDBC_URL);
	    }
	    return connection;
	} catch (Exception e) {
	    return null;
	}

    }

    /**
     * Stores the translation into a specific target language of a given text
     * query in the database. To avoid excessive memory usage, hash of the text
     * is stored instead of the text itself.
     * 
     * If the connection to the database is not possible, said translation won't
     * be stored and no access will be carried out (either for retrieving or
     * saving to DB).
     * 
     * @param text           sentence to be translated to the target language
     * @param translation    translated text
     * @param targetLanguage alpha2 code of the target language (i.e. en_US)
     * @throws SQLException in case of connection issue with DB
     */
    private boolean store(String text, String translation,
	    String targetLanguage) {

	boolean stored = false;
	if (getConnection() == null) {
	    return stored;
	}

	try (PreparedStatement stmnt = getConnection()
		.prepareStatement(INSERT)) {

	    // Hash - translated text - creation - language code
	    String hashOriginal = hash(text);
	    String textTranslated = new String(
		    translation.getBytes(StandardCharsets.UTF_8),
		    StandardCharsets.UTF_8);
	    Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	    String code = targetLanguage.toLowerCase().replace("-", "_");

	    // Set params
	    stmnt.setString(1, hashOriginal); // PK
	    stmnt.setString(2, textTranslated);
	    stmnt.setTimestamp(3, createdAt);
	    stmnt.setString(4, code); // PK

	    stmnt.executeUpdate();
	    stored = true;
	} catch (SQLException e) {
	    // already saved!
	    stored = false;
	} catch (Exception e) {
	    stored = false;
	}

	return stored;
    }

    /**
     * Checks whether a given text has already been translated into a given
     * language and saved into the database. If so, its retrieves the
     * translation.
     * 
     * @param text:     text in its original language
     * @param language: language code
     * 
     * @return String representing the translation into the language of said
     *         text, null otherwise
     * 
     * @throws TranslationException due to SQL or hashing error (retrieving a
     *                              translation from the cache)
     */
    private String getTranslation(String text, String language)
	    throws TranslationException {
	String trans = null;

	try (PreparedStatement stmnt = getConnection().prepareStatement(FIND)) {
	    // Check primary key (hash of text + language code)
	    try {
		stmnt.setString(1, hash(text));
	    } catch (NoSuchAlgorithmException e) {
		// Not possible to retrieve translation
		return null;
	    } // PK
	    stmnt.setString(2, language.toLowerCase().replace("-", "_"));

	    ResultSet rs = stmnt.executeQuery();
	    if (rs.next()) {
		trans = rs.getString("text");
	    }
	} catch (Exception e) {
	    // Nothing found :(
	}

	return trans;
    }

    /**
     * Converts a given text to their hash representation.
     * 
     * @param text to compute the hash of
     * @return hexadecimal hash representation
     * @throws NoSuchAlgorithmException @ with issues computing hash
     */
    private String hash(String text) throws NoSuchAlgorithmException {
	return HashUtil.getHash(text);
    }
}
