package main.java.api;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatMessage;

import main.java.utils.LocaleFileWriter;

/**
 * Translates a given input .properties file into another one in the specified
 * target language, via OpenAI's ChatCompletions API.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version 2.0 (February 2024)
 */
public class Translator {

    private static TranslationApi api;
    private static LocaleFileWriter file;
    private String results;

    public Translator() {
	api = new TranslationApi(); // API access
	file = new LocaleFileWriter(); // Localization file manager
	reset(); // File initialization
    }

    /**
     * Executes the translation process, from a given input file: establishes
     * target language, accesses API to carry out translation and saves the
     * results as text.
     * 
     * @param targetLang, target language (format: "English, United States")
     * @return translated texts as a string
     * @throws Exception if there is an error with the API
     */
    public void translateTo(String target) throws Exception {
	file.setTargetLanguage(target);
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
	List<ChatMessage> messages = api.setPrompt(properties, sourceLang,
		targetLang);
	return api.translate(messages);
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
