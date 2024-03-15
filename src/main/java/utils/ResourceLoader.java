package main.java.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
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
    private final static String SUPPORTED_LANGUAGES_FILE = "/main/resources/other/languages.txt";
    private final static String FONT_FILE = "/main/resources/other/sf-pro.otf";
    private final static String API_PROPERTIES_FILE = "/main/resources/properties/api.properties";
    private final static String CONFIG_FILE = "/main/resources/properties/config.properties";

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
    public static List<String> getSupportedLanguages() {
	try {
	    URL res = ResourceLoader.class
		    .getResource(SUPPORTED_LANGUAGES_FILE);
	    File f = new File(res.toURI());
	    List<String> list = Files.readAllLines(f.toPath());
	    return list;
	} catch (URISyntaxException | IOException ex) {
	    Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE,
		    null, ex);
	    return null;
	}
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

}
