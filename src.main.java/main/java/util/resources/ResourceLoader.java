package main.java.util.resources;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;

/**
 * Access to resources of interest to the app and its GUI.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class ResourceLoader {

    /**
     * File paths to respective resources
     */
    private final static String LANGUAGES_FILE = "/locale/languages_en.txt";
    private final static String FONT_FILE = "/img/sf-pro.otf";
    private final static String API_PROPERTIES_FILE = "/properties/api.properties";
    private final static String CONFIG_FILE = "/properties/config.properties";
    private final static String DATABASE_NAME = "/database/cache.db";
    private final static String SPEECHCODES_FILE = "/locale/speech-language_codes.txt";

    /**
     * @return default font for the GUI of the program
     * @throws ResourceException in case of font not found in resources dir
     */
    public static Font getFont() throws ResourceException {
	try {
	    InputStream is = ResourceLoader.class
		    .getResourceAsStream(FONT_FILE);
	    Font font = Font.createFont(Font.TRUETYPE_FONT, is);
	    return font;
	} catch (Exception e) {
	    throw new ResourceException(FONT_FILE);
	}
    }

    /**
     * @return api settings for ChatCompletions API
     */
    public static Properties getApiSettings() throws PropertiesException {
	try (InputStream is = ResourceLoader.class
		.getResourceAsStream(API_PROPERTIES_FILE)) {
	    Properties pr = new Properties();
	    pr.load(is);
	    return pr;
	} catch (Exception e) {
	    throw new PropertiesException(API_PROPERTIES_FILE, true);
	}
    }

    public static String getApiKey() throws ResourceException {
	return getPropertyFromConfig("API_KEY");
    }

    public static String getAzureSpeechApiKey() throws ResourceException {
	return getPropertyFromConfig("AZURE_API_KEY_1");
    }

    public static String getAzureVisionApiKey() throws ResourceException {
	return getPropertyFromConfig("AZURE_CV_API_KEY_1");
    }

    public static String getAzureVisionEndpoint() throws ResourceException {
	return getPropertyFromConfig("AZURE_CV_ENDPOINT");
    }

    private static String getPropertyFromConfig(String propertyName)
	    throws ResourceException {
	try (InputStream is = ResourceLoader.class
		.getResourceAsStream(CONFIG_FILE)) {
	    Properties pr = new Properties();
	    pr.load(is);
	    return pr.getProperty(propertyName);
	} catch (Exception e) {
	    throw new ResourceException(propertyName);
	}
    }

    /**
     * @param resource bundle with localized messages
     * @return map of all supported languages of the program in its locale, with
     *         their index in the real English list as value
     */
    public static Map<String, Integer> getMapSupportedLanguages(
	    ResourceBundle messages) {
	List<String> list = getAvailableLanguages(messages);
	Map<String, Integer> mapLanguages = new HashMap<>();
	for (int i = 0; i < list.size(); i++) {
	    mapLanguages.put(list.get(i), i);
	}
	return mapLanguages;
    }

    /**
     * @return map of all supported languages of the program in English, with
     *         their index as key
     * @throws ResourceException in case of issues during IO and resource
     *                           processing
     */
    public static Map<Integer, String> getMapSupportedLanguagesInEnglish()
	    throws ResourceException {
	try (InputStream inputStream = ResourceLoader.class
		.getResourceAsStream(LANGUAGES_FILE);
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(inputStream))) {
	    Map<Integer, String> mapLanguages = new HashMap<>();
	    String line;
	    int lineNumber = 0;
	    while ((line = reader.readLine()) != null) {
		mapLanguages.put(lineNumber, line.trim()); // Using trim to
		lineNumber++;
	    }
	    return mapLanguages;
	} catch (IOException e) {
	    throw new ResourceException(LANGUAGES_FILE);
	}
    }

    /**
     * @return JDBC url of the translation database for the application
     * @throws ResourceException if database is not found in resource directory
     */
    public static String getJdbcUrl() throws ResourceException {
	try {
	    URI jdbc = ResourceLoader.class.getResource(DATABASE_NAME).toURI();
	    return "jdbc:sqlite:" + Paths.get(jdbc).toString();
	} catch (URISyntaxException e) {
	    throw new ResourceException(DATABASE_NAME);
	}
    }

    /**
     * @param path absolute path to a file
     * @return file extension of the file, if it exists
     */
    public static Optional<String> getFileExtension(String path) {
	return Optional.ofNullable(path).filter(f -> f.contains("."))
		.map(f -> f.substring(path.lastIndexOf(".") + 1));
    }

    /**
     * @param messages: localization of the application
     * @return array of language names to which the app can effectively be
     *         localized to
     */
    public static String[] getLanguageNamesForMenu(ResourceBundle messages) {
	return messages.getString("languages").split("-");
    }

    /**
     * @param messages: localization of the application
     * @return list of all supported languages and localization of the program
     */
    public static List<String> getAvailableLanguages(ResourceBundle messages) {
	String languages = messages.getString("file.languages");
	String[] supportedLanguages = languages.split("-");
	return Arrays.asList(supportedLanguages);
    }

    /**
     * @return list of all language codes supported by the speech API
     */
    public static List<String> getSupportedLanguages_Speech(
	    ResourceBundle messages) throws ResourceException {
	try (InputStream is = ResourceLoader.class
		.getResourceAsStream(SPEECHCODES_FILE);
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(is))) {
	    List<String> codes = new ArrayList<>();
	    String line;

	    String spec;
	    String global;
	    while ((line = reader.readLine()) != null) {
		spec = line;
		global = line.split("-")[0];

		// Add both specific + global code
		codes.add(spec);
		if (!codes.contains(global)) {
		    codes.add(global);
		}

	    }
	    return codes;
	} catch (Exception e) {
	    throw new ResourceException(SPEECHCODES_FILE);
	}
    }

}
