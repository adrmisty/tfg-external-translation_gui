package main.java.logic.translation;

import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.speech.Speech;
import main.java.logic.translation.mode.AutoTranslation;
import main.java.logic.translation.mode.ManualTranslation;
import main.java.logic.util.file.LocaleFileWriter;
import main.java.logic.util.properties.ResourceLoader;

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

    private static LocaleFileWriter file;

    // Text to speech
    private Speech speech;

    // Translation mode
    private TranslationMode auto;
    private TranslationMode manual;
    private TranslationMode mode;

    // Translations
    private Properties results; // Translation results

    /*
     * ######################## PUBLIC METHODS #################################
     */

    /**
     * Creates a generic translator.
     * 
     * @param messages localization settings chosen by the user
     * @throws Exception in case of I/O issues with writing to file
     */
    public Translator(ResourceBundle messages) throws Exception {
	file = new LocaleFileWriter(messages); // Results file
	speech = new Speech(); // TTS
	reset(); // Initialization
    }

    /**
     * Sets automatic translation mode.
     * 
     * @throws Exception with problems creating the API access service
     */
    public void setAutoMode() throws Exception {
	if (auto == null) {
	    auto = new AutoTranslation(file);
	}
	this.mode = auto;
    }

    /**
     * Sets manual translation mode.
     * 
     * @param path to which manual writing will be done
     */
    public void setManualMode(String path) {
	if (manual == null) {
	    manual = new ManualTranslation(file, path);
	}
	this.mode = manual;
    }

    /**
     * Translates according to the specified translation mode, into a given
     * target language.
     * 
     * @param language format "Bulgarian, Bulgaria"
     * @return results in the form of a Properties object
     * @throws Exception in case of translation problems
     */
    public Properties translateTo(String language) throws Exception {
	// For file creation & processing
	file.setTargetLanguage(language);
	// Translation mode
	this.results = mode.translate(language);
	return this.results;
    }

    /**
     * Configures to the target language, and has the Text-to-Speech
     * functionality execute reading over the results. [i.e. if translated texts
     * are in Bulgarian, voice type will be localized to Bulgarian so its accent
     * is correctly captured]
     * 
     * @param boolean true if start playing, false if stop playing
     * @throws Exception in case of issue with TTS configuration/run
     */
    public void toSpeech(boolean start) throws Exception {
	if (start) {
	    speech.speak(file.getTargetCode(), this.results);
	} else {
	    speech.stop();
	}
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
     * @throws Exception
     */
    public void save(String path) throws Exception {
	if (!file.isFileTemporary()) {
	    file.write(path, this.results);
	} else {
	    file.saveReview(path);
	    // Save review updates in results
	    update();
	}
    }

    /**
     * Writes the results into a temporary file to be used for reviewing and
     * editing the translation.
     * 
     * @throws Exception
     */
    public String review() throws Exception {
	return file.tempWrite(this.results);
    }

    public void update() throws Exception {
	this.results = ResourceLoader.loadProperties(getSavedFilePath());
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
	this.results = new Properties();
	if (mode != null) {
	    mode.reset();
	}
	file.reset();
    }

    /**
     * @return boolean true if translation process is over successfully, false
     *         otherwise
     */
    public boolean isDone() {
	return !results.isEmpty();
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
