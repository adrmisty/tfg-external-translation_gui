package main.java.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties file parser.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version 1.0 (February 2024)
 */
public class I18NFileParser {

    // #TODO set locale code as that of the user's computer
    private static final String _DEFAULT_LANG_CODE = "en";
    private static final String _DEFAULT_COUNTRY_CODE = "US";

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

	String[] splitName = filepath.split("\\.");
	String extension = splitName[splitName.length - 1];

	// Not a valid file
	if (!extension.equals("properties")) {
	    throw new Exception(
		    "The indicated localization file is not i18n-compliant!");
	}

	String[] filenameSplit = splitName[0].split("_", 3);
	String file = filenameSplit[0];
	String codes = (filenameSplit.length > 1) ? filenameSplit[1] : null;

	if (codes == null) {
	    codes = _DEFAULT_LANG_CODE + "_" + _DEFAULT_COUNTRY_CODE;
	}

	// Save bundle name and its locale
	Map<String, String> fileInfo = new HashMap<String, String>();
	fileInfo.put("bundleName", file);
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
