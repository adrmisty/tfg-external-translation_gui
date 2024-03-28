package main.java.translate.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import main.java.util.HashUtil;
import main.java.util.PropertiesUtil;
import main.java.util.ResourceLoader;

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

    private String JDBC_URL;
    private Connection connection;

    /**
     * Closes the connection to the caché database.
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
     * @throws Exception
     */
    public void storeAll(Properties results, Properties originals,
	    String language) throws Exception {

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
     * @throws Exception in case of SQL exception
     */
    public void reset() throws Exception {
	getConnection();
	String delete = "DELETE FROM translation_cache";

	try (PreparedStatement stmnt = connection.prepareStatement(delete)) {
	    stmnt.executeUpdate();
	}
	closeConnection();
    }

    /**
     * Retrieves all present translations of a given set of properties into a
     * language, that are in the database. This set of properties can be empty.
     * 
     * @param properties: properties NOT translated in the database
     * @param language:   alpha2 code of the language
     * @return a list of Properties objects: the first is the set of
     *         translations already found in the cache, the second is those
     *         untranslated objects
     * @throws Exception
     */
    public Properties[] getCachedTranslations(Properties properties,
	    String language) throws Exception {

	Properties translations = new Properties();
	Properties untranslated = new Properties();
	List<String> keys = PropertiesUtil.getKeys(properties);

	for (String key : keys) {
	    String text = properties.getProperty(key);
	    String translation = getTranslation(text, language);

	    if (translation != null) {
		translations.put(key, translation);
	    } else {
		untranslated.put(key, text);
	    }
	}

	closeConnection();
	return new Properties[] { translations, untranslated };
    }

    /*
     * ###################################################### AUX
     */

    /**
     * @return established connection to the SQLite database
     * @throws Exception in case of error with JDBC url or connection
     *                   establishment
     */
    private Connection getConnection() throws Exception {
	if (JDBC_URL == null) {
	    JDBC_URL = ResourceLoader.getJdbcUrl();
	}
	if (connection == null || connection.isClosed()) {
	    connection = DriverManager.getConnection(JDBC_URL);
	}
	return connection;
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
	    String targetLanguage) throws Exception {

	getConnection();
	boolean stored;

	String insert = "INSERT INTO translation_cache (text_hash, text_translation,"
		+ " created_at, language_code) VALUES (?, ?, ?, ?)";
	try (PreparedStatement stmnt = connection.prepareStatement(insert)) {

	    // Save hash, translation, language code and timestamp
	    stmnt.setString(1, hash(text)); // PK
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
     * @throws Exception in case of SQL error or hashing error
     */
    private String getTranslation(String text, String language)
	    throws Exception {
	getConnection();
	String find = "SELECT text_translation as text FROM translation_cache WHERE text_hash=? and language_code=?";
	String trans = null;

	try (PreparedStatement stmnt = connection.prepareStatement(find)) {
	    // Check primary key (hash of text + language code)
	    stmnt.setString(1, hash(text)); // PK
	    stmnt.setString(2, language.toLowerCase());

	    ResultSet rs = stmnt.executeQuery();
	    if (rs.next()) {
		trans = rs.getString("text");
	    }
	}

	return trans;
    }

    private String hash(String text) throws Exception {
	return HashUtil.getHash(text);
    }

}
