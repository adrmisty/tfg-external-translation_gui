package main.java.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Manager for the processing and writing of a file's translation.
 * 
 * Contains relevant information to a file that is currently being processed by
 * the application (properties contents, original bundle, and source/target
 * languages).
 * 
 * Deals with writing respective results to [temporary] files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class LocaleFileWriter {

    // File processor
    private LocaleFileProcessor fileProcessor;

    // Languages
    private String sourceLanguage; // "English, United States"
    private Locale targetLanguage; // "Azerbaijani, Azerbaijan" or "az-AZ"

    // Current file information
    private Properties properties; // Information in file
    private String bundleName; // Bundle name for the app's .properties files
    private String savedFilePath; // Absolute file path to translated file
    private String savedFileName; // Bundle name + new locale code for file

    // Temporary file information
    private Path temporaryFile;
    private boolean isFileTemporary = false;

    public LocaleFileWriter() {
	fileProcessor = new LocaleFileProcessor();
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
	this.properties = ResourceLoader.loadProperties(filepath);
	Map<String, String> fileInfo = fileProcessor.getFileInfo(filepath);
	this.sourceLanguage = LocaleNameParser
		.getLanguage(fileInfo.get("locales"));
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
	writeResults(writer, text);
	writer.close();
    }

    /**
     * For manual translation: upon choosing the directory where to save the
     * manual translation, they are prompted to write their translation onto the
     * new .properties file.
     * 
     * @param path: path of the directory onto which to write their translation
     * @return file path of the translation file in the specific directory
     * @throws IOException
     */
    public String manualWrite(String path) throws IOException {
	setSavedFileName();
	setSavedFilePath(path);
	BufferedWriter writer = new BufferedWriter(
		new FileWriter(savedFilePath));
	writeKeys(writer);
	writer.close();
	return this.savedFilePath;
    }

    /**
     * From the temporary file that was used during reviewing, save these
     * results onto the specified directory.
     * 
     * @param path: directory specification onto which the file should be saved
     * @throws IOException
     */
    public void saveReview(String path) throws IOException {
	setSavedFilePath(path);
	Path destination = Paths.get(this.savedFilePath);
	Files.move(temporaryFile, destination);
    }

    /**
     * For reviewing the translation: writes the results into a temporary file
     * which, if the user wants, can be disposed of. Otherwise, they can save
     * the results into a specific directory.
     * 
     * @param text: results of the translation to be written onto the file
     * @return path to temporary file
     * @throws IOException
     */
    public String tempWrite(String text) throws IOException {
	setSavedFileName();
	return writeTempResults(text).toAbsolutePath().toString();
    }

    /*
     * SETTERS & GETTERS
     * ########################################################################
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
	this.temporaryFile = null;
	this.isFileTemporary = false;
    }

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
	this.savedFilePath = this.savedFilePath.replaceAll("\\d", ""); // no
								       // numbers
    }

    private void setSavedFileName() {
	String localName = bundleName + "_" + targetLanguage.toLanguageTag()
		+ ".properties";
	this.savedFileName = LocaleNameParser.formatName(localName);
    }

    /**
     * Saves the chosen target language (country-specific) as the display name
     * of the language.
     * 
     * @param index of the targetLanguage, format: "English, United States"
     * @throws Exception if specified Locale is not supported yet
     */
    public void setTargetLanguage(int index) throws Exception {
	this.targetLanguage = fileProcessor.getTargetLanguage(index);
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

    /**
     * @return boolean true if the contents from the temporary file should be
     *         saved onto the chosen path, or if this is a new file
     */
    public boolean isFileTemporary() {
	return isFileTemporary;
    }

    /*
     * ######################## AUXILIARY METHODS #############################
     */

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
    private void writeResults(BufferedWriter writer, String text)
	    throws IOException {
	String props = fileProcessor
		.getWrittenResults(getTargetLanguage(), text, properties)
		.toString();
	writer.write(props);
	writer.close();
    }

    /**
     * Write a given set of keys onto a file.
     * 
     * @param writer (bufferedWriter with a reference to the file)
     * 
     * @throws IOException in case of an issue with the bufferedWriter
     */
    private void writeKeys(BufferedWriter writer) throws IOException {
	String keys = fileProcessor.getKeysText(getTargetLanguage(), properties)
		.toString();
	writer.write(keys);
	writer.close();
    }

    /**
     * Writes results onto a temporary file, to be used for reviewing.
     * 
     * @param results of the translation
     * @return Path object representing the temporary file
     * @throws IOException
     */
    private Path writeTempResults(String text) throws IOException {
	String name = this.savedFileName.substring(0,
		this.savedFileName.indexOf("."));
	this.temporaryFile = Files.createTempFile(name, ".properties");
	Files.writeString(temporaryFile, fileProcessor
		.getWrittenResults(getTargetLanguage(), text, properties));
	this.isFileTemporary = true;
	return temporaryFile;
    }

}
