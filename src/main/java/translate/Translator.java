package main.java.translate;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.translate.api.ApiTranslation;
import main.java.translate.database.TranslationCache;
import main.java.util.PropertiesUtil;
import main.java.util.file.LocaleFileWriter;

/**
 * Translates a given input .properties file into another one in the specified
 * target language, via OpenAI's ChatCompletions API.
 * 
 * This translator manages a file writer and can access a database which serves
 * as a translation cache.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class Translator {

    private static ApiTranslation api;
    private static LocaleFileWriter file;
    private TranslationCache cache;

    // Translations
    private Properties results; // Translation results

    /*
     * ######################## PUBLIC METHODS #################################
     */

    /**
     * Creates a translator with a given localization for the application.
     * 
     * @param messages: localization settings chosen by the user
     * @throws Exception in case of I/O issues with writing to file
     */
    public Translator(ResourceBundle messages) throws Exception {
	api = new ApiTranslation(); // API access
	file = new LocaleFileWriter(messages); // Localization file manager
	cache = new TranslationCache(); // Translation database
	reset(); // Initialization
    }

    /**
     * Executes the automatic API translation process, from a given input file:
     * 
     * - 1. Establishes the target language.
     * 
     * - 2. Accesses LL to carry out automatic translation. (In case of
     * translations having already been carried out in the past, result is
     * retrieved directly from the caché).
     * 
     * - 3. Retrieves results.
     * 
     * - 4. Updates caché with new translations.
     * 
     * @param target language: with format "English, United States"
     * 
     * @throws Exception if there is an error with API access
     */
    public void autoTranslateTo(String language) throws Exception {
	file.setTargetLanguage(language);
	this.results = apiTranslate();
    }

    /**
     * Writes property keys into a new file so that the user can execute the
     * manual translation themselves.
     * 
     * @param language: string representation of the language in the program's
     *                  locale (i.e. if program is being executed in French and
     *                  results shall be translated to "German", this string
     *                  would be "Allemand")
     * @param path:     path of the directory where to create and write the
     *                  translated file
     * @throws Exception in case of issue with I/O writing to file
     */
    public String manualTranslateTo(String language, String path)
	    throws Exception {
	file.setTargetLanguage(language);
	return file.manualWrite(path);
    }

    /**
     * Inputs the file that the user wishes to translate, which must be
     * compliant with i18n format.
     * 
     * If not, an exception is thrown. Otherwise, the file is parsed and its
     * relevant information saved for further processing.
     * 
     * @param path: absolute path to the file to be translated
     * @throws Exception if file is not format-compliant
     */
    public void input(String path) throws Exception {
	file.parse(path);
    }

    /**
     * Saves results onto chosen file path.
     * 
     * If said results have already been written to a temporary review file,
     * they are copied onto the new path. Otherwise, results are written for the
     * first time in the specified file path.
     * 
     * @param file path: absolute directory path where file should be saved
     * @throws IOException in case of I/O issue when writing to/from file, or
     *                     copying contents of temporary file
     */
    public void save(String path) throws IOException {
	if (!file.isFileTemporary()) {
	    file.write(path, this.results);
	} else {
	    file.saveReview(path);
	}
    }

    /**
     * Writes the results into a temporary file to be used for reviewing and
     * editing the translation.
     * 
     * @throws IOException in case of issues with I/O writing to temporary file
     */
    public String review() throws IOException {
	return file.tempWrite(this.results);
    }

    /*
     * ######################## AUXILIARY METHODS ##############################
     */

    /**
     * Accesses API to translate a set of properties from a source language to a
     * target language.
     * 
     * @return translated properties
     * @throws Exception in case of issue with API
     */
    private Properties apiTranslate() throws Exception {
	// Checks whether some translations have already been made
	fromCache();
	// Translate strictly those that have never been translated before
	Properties results = getAutoResults();
	// Update cache
	toCache();
	// Retrieve results
	return results;
    }

    /**
     * If any, saves API results onto translation cache (if not found in the
     * database!).
     * 
     * @throws Exception in case of issue with DB
     */
    private void toCache() throws Exception {
	if (api.getResults() != null) {
	    cache.storeAll(api.getResults(), file.getProperties(),
		    file.getTargetLanguageCode());
	}
    }

    /**
     * Retrieves those values already translated and present in the database,
     * and sets the group of properties to be translated and sent to the API.
     * 
     * @throws Exception in case of issue with DB
     */
    private void fromCache() throws Exception {
	cache.match(file.getProperties(), file.getTargetLanguageCode());
    }

    /**
     * Resets all remaining file-related information, to [re-]start the
     * translation process. [Optionally: remove everything from the DB]
     * 
     * @throws Exception in case of issue with DB
     */
    public void reset() throws Exception {
	cache.reset();
	this.results = new Properties();
	file.reset();
    }

    /**
     * @return boolean true if translation process is over successfully, false
     *         otherwise
     */
    public boolean isDone() {
	return !this.results.isEmpty();
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

    /**
     * Considering the translations found in the cache and those yet to be done,
     * it either does/doesn't request translations to the API, for optimization
     * of performance.
     * 
     * @return combined results (caché, API...)
     * @throws Exception in case of error with API translation
     */
    private Properties getAutoResults() throws Exception {

	// No need to access the API
	if (cache.getUntranslated().isEmpty()) {
	    return cache.getTranslated();

	} else {
	    // Needs to access the API
	    api.translate(cache.getUntranslated(), file.getSourceLanguage(),
		    file.getTargetLanguage());

	    // Everything has been translated from API
	    if (cache.getTranslated().isEmpty()) {
		return api.getResults();

		// Both API and cache translations
	    } else {
		return PropertiesUtil.join(api.getResults(),
			cache.getTranslated());
	    }
	}

    }
}
