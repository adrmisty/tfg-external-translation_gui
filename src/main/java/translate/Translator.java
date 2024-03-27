package main.java.translate;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.translate.api.ApiTranslation;
import main.java.translate.database.TranslationCache;
import main.java.util.file.LocaleFileWriter;
import main.java.util.file.PropertiesUtil;

/**
 * Translates a given input .properties file into another one in the specified
 * target language, via OpenAI's ChatCompletions API. This translator manages a
 * file writer and can manage and access a database which serves as a
 * translation cache.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class Translator {

    private static ApiTranslation api;
    private static LocaleFileWriter file;
    private TranslationCache cache;

    // Translations
    private Properties results; // Complete results
    private Properties apiResults; // Retrieved by the API
    private Properties notInCache; // To be submitted to the API
    private Properties inCache; // Found in the cache

    public Translator(ResourceBundle messages) throws Exception {
	api = new ApiTranslation(); // API access
	file = new LocaleFileWriter(messages); // Localization file manager
	cache = new TranslationCache(); // Translation database
	reset(); // File initialization
    }

    /**
     * Executes the translation process, from a given input file: establishes
     * target language, accesses API to carry out translation and saves the
     * results as text.
     * 
     * @param the target language (format: "English, United States")
     * @return translated texts as a string
     * @throws Exception if there is an error with the API
     */
    public void translateTo(String language) throws Exception {
	file.setTargetLanguage(language);
	this.results = apiTranslate();
	updateCache();
    }

    /**
     * Inputs the file that the user wishes to translate, which must be
     * compliant with i18n format. If not, an exception is thrown. Otherwise,
     * the file is parsed and its relevant information saved for further
     * processing.
     * 
     * @param (absolute path to the file)
     * @throws Exception if file is not format-compliant
     */
    public void input(String path) throws Exception {
	file.parse(path);
    }

    /**
     * Saves results into chosen file path.
     * 
     * @param file path (absolute directory path where file should be saved)
     * @throws IOException
     */
    public void save(String path) throws IOException {
	if (!file.isFileTemporary()) {
	    file.write(path, this.results);
	} else {
	    file.saveReview(path);
	}
    }

    /**
     * Writes property keys into a new file so that the user can execute the
     * manual translation.
     * 
     * @param language: string representation of the language in the program's
     *                  locale
     * @param path:     path of the directory where to write the translation
     * @return path: path of the file with the translation
     * @throws Exception
     */
    public String manualTranslateTo(String language, String path)
	    throws Exception {
	file.setTargetLanguage(language);
	return file.manualWrite(path);
    }

    /**
     * Writes the results into a temporary file to be used for reviewing and
     * editing the translation. Once the user chooses to, the file will be saved
     * to a specific path.
     * 
     * @param file path (absolute directory path where file should be saved)
     * @return path to temporary file as a string
     * @throws IOException
     */
    public String review() throws IOException {
	return file.tempWrite(this.results);
    }

    /*
     * ######################## AUXILIARY METHODS #############################
     */

    /**
     * Accesses API to translate a set of properties from a source language to a
     * target language.
     * 
     * @return translated properties
     * @throws Exception
     */
    private Properties apiTranslate() throws Exception {

	// Checks whether some translations have already been made
	checkCache();

	// Translate strictly those that have never been translated before
	this.apiResults = api.translate(notInCache, file.getSourceLanguage(),
		file.getTargetLanguage());

	// Combine results
	return PropertiesUtil.join(this.inCache, this.apiResults);
    }

    /**
     * Saves API results onto translation cache (if not found in the database!).
     * 
     * @throws Exception
     */
    private void updateCache() throws Exception {
	cache.storeAll(this.apiResults, file.getProperties(),
		file.getTargetLanguageCode());
    }

    /**
     * Retrieves those values already translated and present in the database,
     * and sets the group of properties to be translated and sent to the API.
     * 
     * @throws Exception
     */
    private void checkCache() throws Exception {

	Properties[] props = cache.getCachedTranslations(file.getProperties(),
		file.getTargetLanguageCode());
	this.notInCache = props[1];
	this.inCache = props[0];
    }

    /**
     * Resets all remaining file-related information, to [re-]start the
     * translation process.
     */
    public void reset() {
	this.results = new Properties();
	file.reset();
    }

    /**
     * @return boolean true if translation process is over successfully, false
     *         otherwise
     */
    public boolean isDone() {
	return this.results.isEmpty();
    }

    /**
     * @return saved file path of the newly-translated file
     */
    public String getSavedFilePath() {
	return file.getSavedFilePath();
    }

    /**
     * @return saved file name of the newly-translated file
     */
    public String getSavedFileName() {
	return file.getSavedFileName();
    }

}
