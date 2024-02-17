package main.java.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Properties file parser.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version 1.0 (February 2024)
 */
public class LocaleFile {

    // #TODO set locale code as that of the user's computer
    private static final String _DEFAULT_LANG_CODE = "en";
    private static final String _DEFAULT_COUNTRY_CODE = "US";

    /**
     * Writes an i18n compliant file with translated properties into a given
     * language.
     * 
     * @param path         (absolute directory path)
     * @param bundleName   (bundle name of the localization files)
     * @param localeCode   (identification of the language/country)
     * @param text         (translated properties)
     * @param propertyKeys (names of keys in properties file)
     * 
     * @return true if writing process has been executed correctly
     * @throws IOException
     */
    public static void write(String path, String bundleName, String localeCode,
	    String text, Enumeration<Object> propertyKeys) throws IOException {

	String fileName = path + "/" + bundleName + "_" + localeCode
		+ ".properties";
	BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	writeProperties(writer, text, propertyKeys);
	writer.close();

    }

    private static List<String> writeProperties(BufferedWriter writer,
	    String text, Enumeration<Object> propertyKeys) throws IOException {
	String[] sentences = text.split("\n");
	List<String> props = new ArrayList<>();
	String p = "";

	int i = 0;
	while (propertyKeys.hasMoreElements()) {
	    p = ((String) propertyKeys.nextElement()) + "=" + sentences[i];
	    i++;
	    System.out.println(p);
	    props.add(p);
	    writer.write(p + "\n");
	}

	return props;
    }

    /**
     * Parses a .properties file (i18n-compliant compulsorily) into a Properties
     * object, and retrieves file information such as bundle name and locale
     * codes.
     * 
     * @param filepath of the specified file
     * @return file information (bundle name + local codes), + properties object
     * @throws Exception if file cannot be found, there is an error in
     *                   processing or the file does not comply with the desired
     *                   format
     */
    public static Map<String, Object> parse(String filepath) throws Exception {
	Map<String, Object> parsedInfo = new HashMap<String, Object>();
	parsedInfo.put("fileInfo", getFileProperties(filepath));
	parsedInfo.put("properties", loadProperties(filepath));
	return parsedInfo;
    }

    /**
     * Check whether a file is compliant with the i18n format for localization,
     * and retrieves its related information (bundle name + localization codes
     * of the source language it is written in).
     * 
     * @param filePath of the .properties file
     * @return mapping of file information (bundle name + locale codes in
     *         "xx_XX" format).
     * @throws Exception
     */
    private static Map<String, String> getFileProperties(String filepath)
	    throws Exception {

	if (!ResourceLoader.getFileExtension(filepath).get()
		.equals("properties")) {
	    throw new Exception(
		    "The indicated localization file is not i18n-compliant!");
	}

	String[] fileName = LocaleParser.extractLocaleFromFile(filepath);
	String bundleName = fileName[0];
	String codes = fileName[1];

	if (codes == null) {
	    codes = _DEFAULT_LANG_CODE + "_" + _DEFAULT_COUNTRY_CODE;
	}

	// Save bundle name and its locale
	Map<String, String> fileInfo = new HashMap<String, String>();
	fileInfo.put("bundleName", bundleName);
	fileInfo.put("locales", codes);
	return fileInfo;
    }

    private static Properties loadProperties(String filepath) throws Exception {
	// Parse file into Properties object
	Properties locale = new Properties();
	File file = new File(filepath);

	try (FileInputStream fileStream = new FileInputStream(
		file.getAbsolutePath())) {
	    locale.load(fileStream);
	} catch (FileNotFoundException fnfe) {
	    throw new Exception(fnfe.getMessage());
	} catch (IOException io) {
	    throw new Exception(io.getMessage());
	}

	return locale;

    }

}
