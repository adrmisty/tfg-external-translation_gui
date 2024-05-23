package main.java.logic.translation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.file.FileManager;
import main.java.logic.file.LocaleFile;
import main.java.logic.translation.mode.AutoTranslation;
import main.java.logic.translation.mode.ManualTranslation;
import main.java.logic.translation.mode.TranslationMode;
import main.java.util.exception.IncompleteResultsException;
import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.exception.TranslationException;

/**
 * Manager for the translation and file managing process, combined.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class TranslationManager {

    private static FileManager files;
    private TranslationMode translator;

    public TranslationManager(ResourceBundle messages)
	    throws ResourceException {
	files = new FileManager(messages);
	reset();
    }

    /**
     * Translates a source file, according to the specified translation mode,
     * into a given target language (can be more than 1! - and a maximum of 3).
     * 
     * @throws TranslationException specifically caused by issues with automatic
     *                              translation (api and database access)
     */
    public void translateAll() throws TranslationException {
	for (LocaleFile f : files.getTargetFiles()) {
	    translator.translate(f);
	}
    }

    /*
     * ######################## FILES #################################
     */

    /**
     * Inputs the file that the user wishes to translate.
     * 
     * This file must be compliant with i18n properties format. If not, an
     * exception is thrown. Otherwise, the file is parsed and its relevant
     * information saved for further processing.
     * 
     * @throws PropertiesException if the .properties file deviates from the
     *                             expected format, including issues like
     *                             invalid file name format or incorrect content
     * 
     * @throws IOException         when there are issues encountered during the
     *                             process of reading from or writing to a file,
     *                             such as file not found, permission denied, or
     *                             disk full errors if file is not
     *                             format-compliant
     */
    public void input(String path) throws PropertiesException, IOException {
	files.input(path);
    }

    /**
     * @param directory path where all target files will be saved
     */
    public void to(String directory) {
	files.to(directory);
    }

    /**
     * Saves results of all translations, depending on the chosen translation
     * mo.
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
	files.saveAll();
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
	return files.review();
    }

    /**
     * Includes a set of properties in the new contents of the file.
     * 
     * @param properties to include (as if found in original file)
     */
    public void include(Properties properties) {
	files.include(properties);
    }

    /*
     * ######################## GET/SET ########################################
     */

    /**
     * Resets all remaining file-related information, to [re-]start the
     * translation process.
     */
    public void reset() {
	translator = null;
	files.reset();
    }

    /**
     * @return boolean true if translation process is over successfully, false
     *         otherwise
     */
    public boolean isDone() {
	return files.isDone();
    }

    /**
     * @return list of all resulting target files
     */
    public List<LocaleFile> getTargetFiles() {
	return files.getTargetFiles();
    }

    public String getPath() {
	return files.getTargetPath();
    }

    /**
     * @return source file that has been input to the translator
     */
    public LocaleFile getSource() {
	return files.getSourceFile();
    }

    /**
     * In case that a given property has not been translated into the chosen
     * target language, the key with the empty value is included in the target
     * file.
     * 
     * @return boolean true if all properties found in source file have been
     *         translated to the respective target language in all target files
     * @throws IncompleteResultsException in case it has not been possible to
     *                                    complete translations of all keys
     */
    public boolean areResultsComplete() throws IncompleteResultsException {
	return files.areResultsComplete();
    }

    public void setTargetLanguages(List<String> languages, String defaultLang) {
	for (String s : languages) {
	    files.newLanguage(s, s.equals(defaultLang));
	}
    }

    public void setAutoMode() throws ResourceException, SQLException {
	this.translator = new AutoTranslation(files.getSourceFile());
    }

    public void setManualMode() {
	this.translator = new ManualTranslation();
    }

    /**
     * @return absolute paths of manually-translated file
     */
    public List<String> getResultsPaths() {
	return files.getResultsPaths();
    }

}
