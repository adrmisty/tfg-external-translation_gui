package main.java.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileNotFoundException;
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
 * @version 1.0 (February 2024)
 */
public class ResourceLoader {

    public static Optional<String> getFileExtension(String path) {
	return Optional.ofNullable(path).filter(f -> f.contains("."))
		.map(f -> f.substring(path.lastIndexOf(".") + 1));
    }

    public static List<String> getSupportedLanguages() {
	try {
	    URL res = ResourceLoader.class
		    .getResource("/main/resources/other/languages.txt");
	    File f = new File(res.toURI());
	    List<String> list = Files.readAllLines(f.toPath());
	    return list;
	} catch (URISyntaxException | IOException ex) {
	    Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE,
		    null, ex);
	    return null;
	}
    }

    public static Font getFont() {
	try {
	    InputStream is = ResourceLoader.class
		    .getResourceAsStream("/main/resources/other/sf-pro.otf");
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
	    InputStream is = ResourceLoader.class.getResourceAsStream(
		    "/main/resources/properties/api.properties");
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
	    InputStream is = ResourceLoader.class.getResourceAsStream(
		    "/main/resources/properties/config.properties");
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

}
