package main.java.logic.translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.file.FileManager;
import main.java.logic.translation.mode.AutoTranslation;
import main.java.logic.translation.mode.ManualTranslation;
import main.java.logic.translation.mode.TranslationMode;

/**
 * Translates a given input .properties file into another one in the specified
 * target language, via OpenAI's ChatCompletions API.
 * 
 * This translator acts as a controller between file and translation tasks.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class Translator {

    // All translation target file management
    private static FileManager manager;

    // Translation mode
    private TranslationMode auto;
    private TranslationMode mode;
    private TranslationMode manual;

    // Target languages
    private List<String> targetLanguages = new ArrayList<String>();
    private String defaultLanguage = null;

    /*
     * ######################## TRANSLATION #################################
     */

    /**
     * Creates a generic translator.
     * 
     * @param messages localization settings chosen by the user
     * @throws Exception in case of I/O issues with writing to file
     */
    public Translator(ResourceBundle messages) throws Exception {
	manager = new FileManager(messages); // Results file
	// speech = new Speech(); // TTS
	reset(); // Initialization
    }

    /**
     * Sets automatic translation mode.
     * 
     * @throws Exception with problems creating the API access service
     */
    public void setAutoMode() throws Exception {
	if (auto == null) {
	    auto = new AutoTranslation(manager.getSourceFile());
	}
	this.mode = auto;
    }

    /**
     * Sets manual translation mode.
     * 
     * @param path to which manual writing will be done
     */
    public void setManualMode() {
	if (manual == null) {
	    manual = new ManualTranslation();
	}
	this.mode = manual;
    }

    /**
     * @return absolute path of manually-translated file
     */
    public String getManualPath() {
	return manager.getManualPath();
    }

    /**
     * @return absolute path of manually-translated file
     */
    public List<String> getAutoPaths() {
	return manager.getAutoPaths();
    }

    /**
     * Translates a source file, according to the specified translation mode,
     * into a given target language (can be more than 1! - and a maximum of 3).
     * 
     * @throws Exception in case of translation problems
     */
    public void translateAll() throws Exception {

	boolean automatic = (mode instanceof AutoTranslation);
	for (String language : this.targetLanguages) {
	    mode.translate(manager.newLanguage(language,
		    language.equals(defaultLanguage), automatic));
	}
    }

    /*
     * ######################## FILES #################################
     */

    /**
     * Inputs the file that the user wishes to translate, which is located in
     * the formerly established file path.
     * 
     * This file must be compliant with i18n format. If not, an exception is
     * thrown. Otherwise, the file is parsed and its relevant information saved
     * for further processing.
     * 
     * @throws Exception if file is not format-compliant
     */
    public void input() throws Exception {
	manager.input();
    }

    /**
     * Sets the absolute path from which the source file will be read.
     * 
     * @param directory path of the directory
     */
    public void from(String path) {
	manager.from(path);
    }

    /**
     * Sets the directory where all target files will be saved.
     * 
     * @param directory path of the directory
     */
    public void to(String directory) {
	manager.to(directory);
    }

    /**
     * Saves results of all translations.
     * 
     * For each file, if its results have already been written to a temporary
     * review file, they are copied onto the new path. Otherwise, results are
     * written for the first time in their specified file path.
     * 
     * @throws Exception in case of issue writing to file
     */
    public void saveAll() throws Exception {
	manager.saveAll();
    }

    /**
     * Retrieves results of all automatically-translated, with review purposes.
     * These results are processed and returned in a temporary file that can be
     * edited and reviewed by the user.
     * 
     * @return list of paths to all temporary files
     * @throws Exception in case of issue writing to file
     */
    public List<String> review() throws Exception {
	return manager.review();
    }

    /**
     * Includes a set of properties in the new contents of the file.
     * 
     * @param properties to include (as if found in original file)
     */
    public void include(Properties properties) {
	manager.include(properties);
    }

    /*
     * ######################## GET/SET ########################################
     */

    /**
     * Resets all remaining file-related information, to [re-]start the
     * translation process. [Optionally: remove everything from the DB]
     * 
     * @throws Exception in case of issue with DB
     */
    public void reset() throws Exception {
	if (mode != null) {
	    mode.reset();
	}
	this.targetLanguages = new ArrayList<>();
	manager.reset();
    }

    /**
     * @return boolean true if translation process is over successfully, false
     *         otherwise
     */
    public boolean isDone() {
	return manager.isDone();
    }

    /**
     * @return directory where translated files will be stored
     */
    public String getSavedDirectory() {
	return manager.getTargetDirectory();
    }

    /**
     * @return list of target languages with format "Bulgarian, Bulgaria"
     */
    public List<String> getTargetLanguages() {
	List<String> list = new ArrayList<String>(this.targetLanguages);
	return list;
    }

    /**
     * Sets the respective target and default language(s) for the translation.
     * 
     * @param languages   list of target langs. translations will be processed
     *                    in
     * @param defaultLang default language for these translations
     */
    public void setTargetLanguages(List<String> languages, String defaultLang) {
	this.targetLanguages = languages;
	if (defaultLang != null) {
	    this.defaultLanguage = defaultLang;
	}
    }

}
