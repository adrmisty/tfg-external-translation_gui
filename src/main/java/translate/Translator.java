package main.java.translate;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.translate.api.ApiTranslation;
import main.java.util.file.LocaleFileWriter;

/**
 * Translates a given input .properties file into another one in the specified
 * target language, via OpenAI's ChatCompletions API.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class Translator {

    private static ApiTranslation api;
    private static LocaleFileWriter file;
    private String results;

    public Translator(ResourceBundle messages) throws Exception {
	api = new ApiTranslation(); // API access
	file = new LocaleFileWriter(messages); // Localization file manager
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
	this.results = apiTranslate(file.getProperties(),
		file.getSourceLanguage(), file.getTargetLanguage());
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
     * @param properties:    Properties object containing i18n localization
     *                       settings with texts in a given language
     * @param sourceLanguage (i.e. "English")
     * @param targetLanguage (i.e. "German")
     * @return translated texts as a string
     * @throws Exception
     */
    private String apiTranslate(Properties properties, String sourceLang,
	    String targetLang) throws Exception {
	return api.translate(properties, sourceLang, targetLang);
    }

    /**
     * Resets all remaining file-related information, to [re-]start the
     * translation process.
     */
    public void reset() {
	this.results = "";
	file.reset();
    }

    /**
     * @return boolean true if translation process is over successfully, false
     *         otherwise
     */
    public boolean isDone() {
	return !results.isBlank();
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
