package main.java.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Access to resources of interest to the app and its GUI.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class ResourceLoader {

    /**
     * File paths to respective resources
     */
    private final static String LANGUAGES_FILE = "/locale/languages_en_US.txt";
    private final static String LANGUAGE_CODES_FILE = "/locale/language_codes.txt";
    private final static String FONT_FILE = "/img/sf-pro.otf";
    private final static String API_PROPERTIES_FILE = "/properties/api.properties";
    private final static String CONFIG_FILE = "/properties/config.properties";
    private final static String DATABASE_NAME = "/database/translation_cache.db";

    /**
     * Parses a .properties file content onto a Properties object.
     * 
     * @param file path of the .properties file to load
     * @return Properties object containing the contents of the file
     * @throws Exception if there is an error while loading properties from a
     *                   given file
     */
    public static Properties loadProperties(String filepath) throws Exception {

	if (isFileI18N(filepath)) {
	    Properties props = new Properties();
	    File file = new File(filepath);

	    try (FileInputStream fileStream = new FileInputStream(
		    file.getAbsolutePath())) {
		props.load(fileStream); // Ignores comments, etc
	    } catch (FileNotFoundException fnfe) {
		throw new Exception(fnfe.getMessage());
	    } catch (IOException io) {
		throw new Exception(io.getMessage());
	    }

	    return props;
	} else {
	    throw new Exception(
		    "The provided file does not comply with i18n-localization format.");
	}

    }

    /**
     * @param path: absolute path to a file
     * @return file extension of the file, if it exists
     */
    public static Optional<String> getFileExtension(String path) {
	return Optional.ofNullable(path).filter(f -> f.contains("."))
		.map(f -> f.substring(path.lastIndexOf(".") + 1));
    }

    /**
     * @return list of all supported languages and localization of the program
     */
    public static List<String> getSupportedLanguages(ResourceBundle messages) {
	String languages = messages.getString("file.languages");
	String[] supportedLanguages = languages.split("-");
	return Arrays.asList(supportedLanguages);
    }

    /**
     * @return default font for the GUI of the program
     */
    public static Font getFont() {
	try {
	    InputStream is = ResourceLoader.class
		    .getResourceAsStream(FONT_FILE);
	    Font font = Font.createFont(Font.TRUETYPE_FONT, is);
	    return font;
	} catch (FontFormatException | IOException ex) {
	    Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE,
		    null, ex);
	    return null;
	}
    }

    public static Properties getApiSettings() {
	try {
	    InputStream is = ResourceLoader.class
		    .getResourceAsStream(API_PROPERTIES_FILE);
	    Properties pr = new Properties();
	    pr.load(is);
	    return pr;
	} catch (FileNotFoundException fnfe) {
	    Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE,
		    null, fnfe);
	    return null;
	} catch (IOException io) {
	    Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE,
		    null, io);
	    return null;
	}
    }

    public static String getApiKey() {
	try {
	    InputStream is = ResourceLoader.class
		    .getResourceAsStream(CONFIG_FILE);
	    Properties pr = new Properties();
	    pr.load(is);
	    return pr.getProperty("API_KEY");
	} catch (FileNotFoundException fnfe) {
	    Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE,
		    null, fnfe);
	    return null;
	} catch (IOException io) {
	    Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE,
		    null, io);
	    return null;
	}
    }

    /**
     * @param file path of input file
     * @return boolean true if input file is a .properties file with i18n
     *         format, false otherwise
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static boolean isFileI18N(String filePath)
	    throws FileNotFoundException, IOException {

	try (BufferedReader reader = new BufferedReader(
		new FileReader(filePath))) {
	    String line;
	    while ((line = reader.readLine()) != null) {

		// Ignore comments and empty lines
		if (line.trim().isEmpty() || line.trim().startsWith("#")) {
		    continue;
		}

		// Check if the line is in key-value format
		if (!line.contains("=")) {
		    return false;
		}

		// Check if the key and value are separated by '='
		String[] parts = line.split("=", 2);
		if (parts.length != 2 || parts[0].trim().isEmpty()
			|| parts[1].trim().isEmpty()) {
		    return false;
		}
	    }
	}

	// All good, it's a fully-formed properties file!
	return true;
    }

    /**
     * @param resource bundle with localized messages
     * @return map of all supported languages of the program in its locale, with
     *         their index in the real English list as value
     */
    public static Map<String, Integer> getMapSupportedLanguages(
	    ResourceBundle messages) {
	List<String> list = getSupportedLanguages(messages);
	Map<String, Integer> mapLanguages = new HashMap<String, Integer>();

	for (int i = 0; i < list.size(); i++) {
	    mapLanguages.put(list.get(i), i);
	}

	return mapLanguages;
    }

    /**
     * @return map of all supported languages of the program in English, with
     *         their index as key
     * @throws Exception
     */
    public static Map<Integer, String> getMapSupportedLanguages_English()
	    throws Exception {
	URL res = ResourceLoader.class.getResource(LANGUAGES_FILE);
	File f = new File(res.toURI());
	List<String> list = Files.readAllLines(f.toPath());
	Map<Integer, String> mapLanguages = new HashMap<Integer, String>();

	for (int i = 0; i < list.size(); i++) {
	    mapLanguages.put(i, list.get(i));
	}
	return mapLanguages;
    }

    /**
     * @param messages: localization of the application
     * @return array of language names to which the app can effectively be
     *         localized to
     */
    public static String[] getLanguageNames(ResourceBundle messages) {
	return messages.getString("languages").split("-");
    }

    /**
     * @return language codes of the respective language names to which the app
     *         can effectively be localized to
     * @throws Exception if the resources file is not found
     */
    public static Map<Integer, String> getLanguageCodes() throws Exception {
	Map<Integer, String> map = new HashMap<Integer, String>();
	URL res = ResourceLoader.class.getResource(LANGUAGE_CODES_FILE);
	File f = new File(res.toURI());
	List<String> list = Files.readAllLines(f.toPath());

	for (int i = 0; i < list.size(); i++) {
	    map.put(i, list.get(i));
	}
	return map;
    }

    /**
     * @return JDBC url of the translation database for the application
     * @throws Exception if database is not found in resource directory
     */
    public static String getJdbcUrl() throws Exception {
	URI jdbc = ResourceLoader.class.getResource(DATABASE_NAME).toURI();

	if (jdbc != null) {
	    String dbUrl = Paths.get(jdbc).toString();
	    return ("jdbc:sqlite:" + dbUrl);
	} else {
	    throw new Exception("ERROR: Database not found among resources.");
	}
    }

}
