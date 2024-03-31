package main.java.translation.cache;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import main.java.util.db.HashUtil;
import main.java.util.exception.ResourceException;
import main.java.util.exception.TranslationException;
import main.java.util.properties.PropertiesUtil;
import main.java.util.properties.ResourceLoader;

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
 * @version March 2024
 */
public class TranslationCache {

    // Connection to database
    private String JDBC_URL;
    private Connection connection;

    // Translations in cache
    private Properties notInCache; // Not found in cache
    private Properties inCache; // Translations found in the cache

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
     * Closes the connection to the cache database.
     * 
     * @throws SQLException in case of error with closing the connection
     */
    public void closeConnection() throws SQLException {
	if (connection != null && !connection.isClosed()) {
	    connection.close();
	}
    }

    /**
     * Stores all translations of the submitted textual queries, to their
     * respective target language.
     * 
     * @param results:   all translations
     * @param originals: original, untranslated texts
     * @param language:  alpha2 code of the target language
     * @throws SQLException
     * @throws Exception
     */
    public void storeAll(Properties results, Properties originals,
	    String language) throws TranslationException, SQLException {

	for (String k : PropertiesUtil.getKeys(results)) {
	    String text = originals.getProperty(k);
	    String translation = results.getProperty(k);
	    // Will only be stored if not already found in the DB
	    store(text, translation, language);
	}
	closeConnection();
    }

    /**
     * Deletes all records from the database.
     * 
     * @throws SQL exception
     */
    public void reset() throws SQLException {
	getConnection();
	String delete = "DELETE FROM translation_cache";

	try (PreparedStatement stmnt = connection.prepareStatement(delete)) {
	    stmnt.executeUpdate();
	}
	closeConnection();
    }

    /**
     * Retrieves all present translations of a given set of properties into a
     * language, that are in the database; and also those that are not cached.
     * 
     * @param properties object containing set of translations to be fully
     *                   computed or not
     * @param language   alpha2 code of the language
     * @throws Exception in case of error with database access, writing
     *                   to/from...
     */
    public void match(Properties properties, String language) throws Exception {

	// Initialize
	this.inCache = new Properties();
	this.notInCache = new Properties();

	// Retrieve translations
	List<String> keys = PropertiesUtil.getKeys(properties);

	for (String key : keys) {
	    String text = properties.getProperty(key);
	    String translation = getTranslation(text, language);

	    if (translation != null) {
		this.inCache.put(key, translation);
	    } else {
		this.notInCache.put(key, text);
	    }
	}
	closeConnection();
    }

    /*
     * ######################## AUXILIARY METHODS ##############################
     */

    /**
     * @return established connection to the SQLite database
     * @throws SQLException      in case of error with JDBC url or connection
     *                           establishment
     * @throws ResourceException in case of resource not found
     */
    private Connection getConnection() throws SQLException {
	try {
	    if (JDBC_URL == null) {
		JDBC_URL = ResourceLoader.getJdbcUrl();
	    }
	    if (connection == null || connection.isClosed()) {
		connection = DriverManager.getConnection(JDBC_URL);
	    }
	    return connection;
	} catch (Exception e) {
	    throw new SQLException("ERROR: Could not load database");
	}

    }

    /**
     * Stores the translation into a specific target language of a given text
     * query in the database. To avoid excessive memory usage, hash of the text
     * is stored instead of the text itself.
     * 
     * @param text:           sentence to be translated to the target language
     * @param translation:    translated text
     * @param targetLanguage: alpha2 code of the target language (i.e. en_US)
     * @throws SQLException in case of connection issue with DB
     */
    private boolean store(String text, String translation,
	    String targetLanguage) throws SQLException {

	getConnection();
	boolean stored;

	String insert = "INSERT INTO translation_cache (text_hash, text_translation,"
		+ " created_at, language_code) VALUES (?, ?, ?, ?)";
	try (PreparedStatement stmnt = connection.prepareStatement(insert)) {

	    // Save hash, translation, language code and time stamp
	    try {
		stmnt.setString(1, hash(text));
	    } catch (NoSuchAlgorithmException e) {
		// Do nothing
		stored = false;
		return stored;
	    } // PK
	    stmnt.setString(2, translation);
	    stmnt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	    stmnt.setString(4, targetLanguage.toLowerCase()); // PK

	    stmnt.executeUpdate();
	    stored = true;
	} catch (SQLException e) {
	    // already saved!
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
     * @return String representing the translation into the language of said
     *         text, null otherwise
     * @throws SQLException
     * @throws Exception    in case of SQL error or hashing error
     */
    private String getTranslation(String text, String language)
	    throws SQLException {
	getConnection();
	String find = "SELECT text_translation as text FROM translation_cache WHERE text_hash=? and language_code=?";
	String trans = null;

	try (PreparedStatement stmnt = connection.prepareStatement(find)) {
	    // Check primary key (hash of text + language code)
	    try {
		stmnt.setString(1, hash(text));
	    } catch (NoSuchAlgorithmException e) {
		// Not possible to retrieve translation
		return null;
	    } // PK
	    stmnt.setString(2, language.toLowerCase());

	    ResultSet rs = stmnt.executeQuery();
	    if (rs.next()) {
		trans = rs.getString("text");
	    }
	}

	return trans;
    }

    /**
     * Converts a given text to their hash representation.
     * 
     * @param text to compute the hash of
     * @return hexadecimal hash representation
     * @throws NoSuchAlgorithmException
     * @throws Exception                with issues computing hash
     */
    private String hash(String text) throws NoSuchAlgorithmException {
	return HashUtil.getHash(text);
    }

}
