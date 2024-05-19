package main.java.logic.translation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.file.FileManager;
import main.java.logic.file.SourceFile;
import main.java.logic.file.TargetFile;
import main.java.logic.translation.mode.AutoTranslation;
import main.java.logic.translation.mode.ManualTranslation;
import main.java.logic.translation.mode.TranslationMode;
import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.exception.TranslationException;

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
     * @param messages localization settings chosen by the user @ in case of I/O
     *                 issues with writing to file
     * 
     * @throws ResourceException when resources are not found or
     *                           incorrectly-formatted
     */
    public Translator(ResourceBundle messages) throws ResourceException {
	manager = new FileManager(messages); // Results file
	reset(); // Initialization
    }

    /**
     * Sets automatic translation mode.
     * 
     * @throws ResourceException in case API access is not possible due to
     *                           missing or incorrect resources
     */
    public void setAutoMode() throws ResourceException, SQLException {
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
    public List<String> getPaths() {
	return manager.getPaths();
    }

    /**
     * Translates a source file, according to the specified translation mode,
     * into a given target language (can be more than 1! - and a maximum of 3).
     * 
     * @throws TranslationException specifically caused by issues with automatic
     *                              translation (api and database access)
     */
    public void translateAll() throws TranslationException {

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
     * @throws PropertiesException if the .properties file deviates from the
     *                             expected format, including issues like
     *                             invalid file name format or incorrect content
     * 
     * @throws IOException         when there are issues encountered during the
     *                             process of reading from or writing to a file,
     *                             such as file not found, permission denied, or
     *                             disk full errors
     * 
     * @ if file is not format-compliant
     */
    public void input() throws PropertiesException, IOException {
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
     * @throws PropertiesException if the .properties file deviates from the
     *                             expected format, including issues like
     *                             invalid file name format or incorrect content
     * 
     * @throws IOException         when there are issues encountered during the
     *                             process of reading from or writing to a file,
     *                             such as file not found, permission denied, or
     *                             disk full errors
     * 
     */
    public void saveAll() throws IOException, PropertiesException {
	manager.saveAll();
    }

    /**
     * Retrieves results of all automatically-translated, with review purposes.
     * These results are processed and returned in a temporary file that can be
     * edited and reviewed by the user.
     * 
     * @return list of paths to all temporary files @ in case of issue writing
     *         to file
     * 
     * @throws IOException either IO or ResultsException, when impossible to
     *                     save/review results
     */
    public List<String> review() throws IOException {
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
     */
    public void reset() {
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
     * @return list of all resulting target files
     */
    public List<TargetFile> getResults() {
	return manager.getResults();
    }

    /**
     * @return directory where translated files will be stored
     */
    public String getSavedDirectory() {
	return manager.getTargetDirectory();
    }

    /**
     * @return source file that has been input to the translator
     */
    public SourceFile getSource() {
	return manager.getSourceFile();
    }

    /**
     * In case that a given property has not been translated into the chosen
     * target language, the key with the empty value is included in the target
     * file.
     * 
     * @return boolean true if all properties found in source file have been
     *         translated to the respective target language in all target files
     */
    public boolean areResultsComplete() {
	int size = getSource().getContent().size();
	boolean complete = true;
	for (TargetFile f : getResults()) {
	    if (size != f.getContent().size()) {
		f.complete();
		complete = false;
	    }
	}
	return complete;
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
