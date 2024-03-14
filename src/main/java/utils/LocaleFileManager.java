package main.java.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Manager and controller for all localization-file-related tasks: parsing,
 * loading, writing, saving & alpha-2 localization codes.
 * 
 * Contains relevanti information to a file that is currently being processed by
 * the application (properties contents, original bundle, and source/target
 * languages).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version February 2024
 */
public class LocaleFileManager {

    // Locale
    private static final String _DEFAULT_CODE = Locale.getDefault()
	    .toLanguageTag();
    private static Map<String, Locale> map;

    // Languages
    private String sourceLanguage; // "English, United States"
    private Locale targetLanguage; // "Azerbaijani, Azerbaijan" or "az-AZ"

    // Current file information
    private Properties properties; // Information in file
    private String bundleName; // Bundle name for the app's .properties files
    private String savedFilePath; // Absolute file path to translated file
    private String savedFileName; // Bundle name + new locale code for file

    public LocaleFileManager() {
	map = LocaleParser.getMap();
    }

    /*
     * ######################## PUBLIC METHODS #############################
     */

    /**
     * Parses a .properties file (i18n-compliant) into a Properties object, and
     * retrieves file information such as bundle name and locale codes and saves
     * it.
     * 
     * @param file path of the specified file
     * @throws Exception if file cannot be found, there is an error in
     *                   processing or the file does not comply with the desired
     *                   format
     */
    public void parse(String filepath) throws Exception {
	// Parse and save info
	this.properties = loadProperties(filepath);
	Map<String, String> fileInfo = getFileInfo(filepath);
	this.sourceLanguage = LocaleParser.getLanguage(fileInfo.get("locales"));
	this.bundleName = fileInfo.get("bundleName");
    }

    /**
     * Writes an i18n compliant file with translated properties into a given
     * language.
     * 
     * @param path       (absolute path of the file to write, containing alpha-2
     *                   code)
     * @param localeCode (alpha-2 code identification of the Language_Country as
     *                   "ll_CC")
     * @param text       (translated textual properties)
     * 
     * @return true if writing process has been executed correctly
     * @throws IOException
     */
    public void write(String path, String text) throws IOException {

	setSavedFileName();
	setSavedFilePath(path);

	BufferedWriter writer = new BufferedWriter(
		new FileWriter(savedFilePath));
	writeProperties(writer, text);
	writer.close();
    }

    /*
     * ######################## GETTERS #############################
     */

    /**
     * @return file name of the newly-translated file, with its bundle name and
     *         locale code
     */
    public String getSavedFileName() {
	return savedFileName;
    }

    /**
     * @return file path of the newly-translated file
     */
    public String getSavedFilePath() {
	return savedFilePath;
    }

    private void setSavedFilePath(String path) {
	this.savedFilePath = path + "/" + this.savedFileName;
    }

    private void setSavedFileName() {
	this.savedFileName = bundleName + "_" + targetLanguage.toLanguageTag()
		+ ".properties";

	this.savedFileName.replace("Latn-", "");
	this.savedFileName.replace("Cyrl-", "");
    }

    /**
     * Saves the chosen target language (country-specific) as the display name
     * of the language.
     * 
     * @param targetLanguage, format: "English, United States"
     * @throws Exception if specified Locale is not supported yet
     */
    public void setTargetLanguage(String targetLanguage) throws Exception {
	this.targetLanguage = LocaleParser.extract(map, targetLanguage);
    }

    /**
     * @return display language of the target language's locale
     */
    public String getTargetLanguage() {
	return targetLanguage.getDisplayLanguage();
    }

    /**
     * @return properties content
     */
    public Properties getProperties() {
	return this.properties;
    }

    /**
     * @return source language string
     */
    public String getSourceLanguage() {
	return this.sourceLanguage;
    }

    /*
     * ######################## AUXILIARY METHODS #############################
     */

    /**
     * Reset all file information.
     */
    public void reset() {
	this.savedFileName = "";
	this.savedFilePath = "";
	this.properties = null;
	this.sourceLanguage = "";
	this.targetLanguage = null;
	this.bundleName = "";
    }

    /**
     * From a given set of translated sentences (that represent the property
     * values of an input property file in another language), they are parsed
     * and written to a given file as "{property_name}={property_text}".
     * 
     * @param writer (bufferedWriter with a reference to the file)
     * @param text   (textual properties to parse)
     * 
     * @throws IOException in case of an issue with the bufferedWriter
     */
    private void writeProperties(BufferedWriter writer, String text)
	    throws IOException {
	String[] sentences = text.split("\n");
	String p = "";

	writer.write("# " + this.getTargetLanguage() + "\n\n");
	int i = 0;
	Enumeration<Object> keys = properties.keys();

	while (keys.hasMoreElements()) {
	    p = ((String) keys.nextElement()) + "=" + sentences[i];
	    writer.write(p + "\n");
	    i++;
	}
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
    private Map<String, String> getFileInfo(String filepath) throws Exception {

	if (!ResourceLoader.getFileExtension(filepath).get()
		.equals("properties")) {
	    throw new Exception(
		    "The indicated localization file is not i18n-compliant!");
	}

	String[] fileName = LocaleParser.extractLocaleFromFile(filepath);
	String bundleName = fileName[0];
	String codes = fileName[1];

	if (codes == null) {
	    codes = _DEFAULT_CODE;
	}

	// Save bundle name and its locale
	Map<String, String> fileInfo = new HashMap<String, String>();
	fileInfo.put("bundleName", bundleName);
	fileInfo.put("locales", codes);

	return fileInfo;
    }

    /**
     * Parses a .properties file content onto a Properties object.
     * 
     * @param filepath
     * @return
     * @throws Exception if there is an error while loading properties from a
     *                   given file
     */
    private Properties loadProperties(String filepath) throws Exception {
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
    }

}
